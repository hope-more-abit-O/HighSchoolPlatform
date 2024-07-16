package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.PostPropertiesStatus;
import com.demo.admissionportal.constants.PostStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.post.PostDeleteRequestDTO;
import com.demo.admissionportal.dto.request.post.PostRequestDTO;
import com.demo.admissionportal.dto.request.post.TagRequestDTO;
import com.demo.admissionportal.dto.request.post.UpdatePostRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.post.*;
import com.demo.admissionportal.entity.Post;
import com.demo.admissionportal.entity.Tag;
import com.demo.admissionportal.entity.Type;
import com.demo.admissionportal.entity.UserInfo;
import com.demo.admissionportal.entity.sub_entity.PostTag;
import com.demo.admissionportal.entity.sub_entity.PostType;
import com.demo.admissionportal.entity.sub_entity.PostView;
import com.demo.admissionportal.entity.sub_entity.id.PostTagId;
import com.demo.admissionportal.entity.sub_entity.id.PostTypeId;
import com.demo.admissionportal.repository.PostRepository;
import com.demo.admissionportal.repository.TagRepository;
import com.demo.admissionportal.repository.TypeRepository;
import com.demo.admissionportal.repository.UserInfoRepository;
import com.demo.admissionportal.repository.sub_repository.PostTagRepository;
import com.demo.admissionportal.repository.sub_repository.PostTypeRepository;
import com.demo.admissionportal.repository.sub_repository.PostViewRepository;
import com.demo.admissionportal.service.PostService;
import com.demo.admissionportal.service.TagService;
import com.demo.admissionportal.util.impl.RandomCodeGeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Post service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostTypeRepository postTypeRepository;
    private final TypeRepository typeRepository;
    private final PostViewRepository postViewRepository;
    private final TagService tagService;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final RandomCodeGeneratorUtil randomCodeGeneratorUtil;
    private final ModelMapper modelMapper;
    private final UserInfoRepository userInfoRepository;

    @Override
    public ResponseData<PostDetailResponseDTO> createPost(PostRequestDTO requestDTO) {
        try {
            if (requestDTO == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Sai request");
            }
            // Insert Post
            Post post = postSave(requestDTO);
            if (post == null) {
                throw new Exception("Save post thất bại!");
            }

            // Insert Type Post
            List<PostType> postTypes = postTypeSave(requestDTO.getListType(), post);
            if (postTypes.isEmpty()) {
                throw new Exception("Save post type thất bại!");
            }

            // Insert Post View
            PostView postView = postViewSave(requestDTO.getCreate_by(), post);
            if (postView == null) {
                throw new Exception("Save post view thất bại!");
            }

            // Check Tag Name if user input duplicate
            List<String> checkTagDuplicate = validateDuplicateTag(requestDTO.getListTag());
            if (!checkTagDuplicate.isEmpty()) {
                throw new Exception("Không được nhập tag trùng nhau");
            }
            // Store list Tags
            List<Integer> tagIds = new ArrayList<>();

            // Insert Tags
            Tag tagsSaved = tagSave(requestDTO.getListTag(), post, tagIds);
            if (tagsSaved == null) {
                throw new Exception("Save tag thất bại!");
            }

            // Insert Tag post
            for (Integer tagId : tagIds) {
                PostTag postTagSaved = postTagSave(tagId, post);
                if (postTagSaved == null) {
                    throw new Exception("Save tag post thất bại!");
                }
            }

            List<Type> listType = new ArrayList<>();
            for (Integer type : requestDTO.getListType()) {
                Type listTypeById = typeRepository.findTypeById(type);
                listType.add(listTypeById);
            }

            List<Tag> listTag = new ArrayList<>();
            for (Integer tag : tagIds) {
                Tag listTagById = tagRepository.findTagById(tag);
                listTag.add(listTagById);
            }
            UserInfo userInfo = userInfoRepository.findUserInfoById(post.getCreateBy());


            PostDetailResponseDTO responseDTO = PostDetailResponseDTO.builder()
                    .postProperties(mapperPostPropertiesResponseDTO(post))
                    .listType(mapperTypeResponseDTO(listType))
                    .listTag(mapperTagResponseDTO(listTag))
                    .create_by(mapperUserInfoResponseDTO(userInfo))
                    .build();

            return new ResponseData<>(ResponseCode.C200.getCode(), "Tạo post thành công", responseDTO);
        } catch (Exception ex) {
            log.error("Error when create post: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), ex.getMessage());
        }
    }


    public PostPropertiesResponseDTO mapperPostPropertiesResponseDTO(Post post) {
        return modelMapper.map(post, PostPropertiesResponseDTO.class);
    }


    public List<TypeResponseDTO> mapperTypeResponseDTO(List<Type> lisType) {
        return lisType.stream()
                .map(tag -> modelMapper.map(tag, TypeResponseDTO.class))
                .collect(Collectors.toList());
    }

    public List<TagResponseDTO> mapperTagResponseDTO(List<Tag> listTag) {
        return listTag.stream()
                .map(type -> modelMapper.map(type, TagResponseDTO.class))
                .collect(Collectors.toList());
    }

    private PostTag postTagSave(Integer tagId, Post post) {
        try {

            PostTagId postTagId = new PostTagId();
            postTagId.setPostId(post.getId());
            postTagId.setTagId(tagId);

            PostTag postTag = new PostTag();
            postTag.setId(postTagId);
            postTag.setPost(post);
            Tag findTag = tagRepository.findTagById(tagId);
            postTag.setTag(findTag);
            postTag.setCreateTime(new Date());
            postTag.setCreateBy(post.getCreateBy());
            postTag.setStatus(PostPropertiesStatus.ACTIVE);
            return postTagRepository.save(postTag);
        } catch (Exception ex) {
            log.error("Error when save post tag: {}", ex.getMessage());
            return null;
        }
    }

    private Tag tagSave(List<String> tags, Post post, List<Integer> tagIds) {
        Tag result = new Tag();
        try {
            for (String tagName : tags) {
                Tag checkTagNameExisted = tagService.checkTagExisted(tagName);
                // Case 1 : Check tag name is existed in database
                if (checkTagNameExisted == null) {
                    TagRequestDTO requestDTO = new TagRequestDTO();
                    requestDTO.setName(tagName.trim());
                    requestDTO.setCreate_by(post.getCreateBy());
                    result = tagService.createTag(requestDTO);

                    // Case 2 : If not then add in database
                } else {
                    result = checkTagNameExisted;
                }
                tagIds.add(result.getId());
            }
            return result;
        } catch (Exception ex) {
            log.error("Error when save tag: {}", ex.getMessage());
            return null;
        }
    }

    private Post postSave(PostRequestDTO requestDTO) {
        try {
            Post post = new Post();
            post.setTitle(requestDTO.getTitle());
            post.setContent(requestDTO.getContent());
            post.setThumnail(requestDTO.getThumnail());
            post.setCreateBy(requestDTO.getCreate_by());
            post.setCreateTime(new Date());
            post.setStatus(PostStatus.ACTIVE);
            post.setLike(0);
            post.setView(0);
            post.setUrl("/" + convertURL(requestDTO.getTitle()) + "-" + randomCodeGeneratorUtil.generateRandomString());
            return postRepository.save(post);
        } catch (Exception ex) {
            log.error("Error when save post: {}", ex.getMessage());
            return null;
        }
    }

    private List<PostType> postTypeSave(List<Integer> typeIdList, Post post) {
        List<PostType> postTypes = new ArrayList<>();
        try {
            for (Integer typeId : typeIdList) {
                PostTypeId postTypeId = new PostTypeId(post.getId(), typeId);
                PostType postType = new PostType();
                postType.setId(postTypeId);
                postType.setPost(post);
                Type findType = typeRepository.findTypeById(typeId);
                postType.setType(findType);
                postType.setCreateTime(new Date());
                postType.setCreateBy(post.getCreateBy());
                postType.setStatus(PostPropertiesStatus.ACTIVE);
                postTypes.add(postTypeRepository.save(postType));
            }
        } catch (Exception ex) {
            log.error("Error when save post type: {}", ex.getMessage());
        }
        return postTypes;
    }

    private PostView postViewSave(Integer createBy, Post post) {
        try {
            PostView postView = new PostView();
            postView.setId(post.getId());
            postView.setCreateTime(post.getCreateTime());
            postView.setViewCount(0);
            postView.setLikeCount(0);
            postView.setCreateBy(createBy);
            postView.setStatus(PostPropertiesStatus.ACTIVE);
            return postViewRepository.save(postView);
        } catch (Exception ex) {
            log.error("Error when save post view: {}", ex.getMessage());
            return null;
        }
    }

    @Override
    public ResponseData<String> changeStatus(PostDeleteRequestDTO requestDTO) {
        try {
            if (requestDTO == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Sai request");
            }

            // Remove post
            Post resultChangeStatusPost = changeStatusPost(requestDTO);
            if (resultChangeStatusPost == null) {
                throw new Exception("Xoá post thất bại");
            }
            // Remove post tag
            List<Integer> tagIds = new ArrayList<>();
            List<PostTag> resultChangeStatusPostTag = changeStatusPostTag(requestDTO, tagIds);
            if (resultChangeStatusPostTag == null) {
                throw new Exception("Thay đổi trạng thái  post tag thất bại");
            }
            // Remove Post Type
            boolean resultChangeStatusPostType = changeStatusPostType(requestDTO);
            if (!resultChangeStatusPostType) {
                throw new Exception("Thay đổi trạng thái  post type thất bại");
            }

            // Remove Post View
            boolean resultChangeStatusPostView = changeStatusPostView(requestDTO);
            if (!resultChangeStatusPostView) {
                throw new Exception("Thay đổi trạng thái post view thất bại");
            }
            return new ResponseData<>(ResponseCode.C200.getCode(), "Thay đổi trạng thái thành công");
        } catch (Exception ex) {
            log.error("Error when delete post: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), ex.getMessage());
        }
    }

    private boolean changeStatusPostView(PostDeleteRequestDTO requestDTO) {
        try {
            PostView postView = postViewRepository.findPostViewById(requestDTO.getPostId());
            if (postView == null) {
                return false;
            }
            if (postView.getStatus().equals(PostPropertiesStatus.INACTIVE)) {
                postView.setStatus(PostPropertiesStatus.ACTIVE);
            } else {
                postView.setStatus(PostPropertiesStatus.INACTIVE);
            }
            postView.setUpdateTime(new Date());
            postView.setUpdateBy(requestDTO.getUserId());
            postViewRepository.save(postView);
            return true;
        } catch (Exception ex) {
            log.error("Error when remove post view : {}", ex.getMessage());
            return false;
        }
    }

    private boolean changeStatusPostType(PostDeleteRequestDTO requestDTO) {
        try {
            List<PostType> posts = postTypeRepository.findPostTypeByPostId(requestDTO.getPostId());
            for (PostType post : posts) {
                if (post.getStatus().equals(PostPropertiesStatus.INACTIVE)) {
                    post.setStatus(PostPropertiesStatus.ACTIVE);
                } else {
                    post.setStatus(PostPropertiesStatus.INACTIVE);
                }
                post.setUpdateTime(new Date());
                post.setUpdateBy(requestDTO.getUserId());
                postTypeRepository.save(post);
            }
            return true;
        } catch (Exception ex) {
            log.error("Error when remove post type : {}", ex.getMessage());
            return false;
        }
    }


    private Post changeStatusPost(PostDeleteRequestDTO requestDTO) {
        try {
            Post post = postRepository.findFirstById(requestDTO.getPostId());
            if (post == null) {
                return null;
            }
            if (post.getStatus().equals(PostStatus.INACTIVE)) {
                post.setStatus(PostStatus.ACTIVE);
            } else {
                post.setStatus(PostStatus.INACTIVE);
            }
            post.setUpdateTime(new Date());
            post.setUpdateBy(requestDTO.getUserId());
            return postRepository.save(post);

        } catch (Exception ex) {
            log.error("Error when remove post : {}", ex.getMessage());
            return null;
        }
    }

    private List<PostTag> changeStatusPostTag(PostDeleteRequestDTO requestDTO, List<Integer> tagIds) {
        try {
            List<PostTag> postTagList = postTagRepository.findPostTagByPostId(requestDTO.getPostId());
            if (postTagList == null) {
                return null;
            }
            for (PostTag postTag : postTagList) {
                if (postTag.getStatus().equals(PostPropertiesStatus.INACTIVE)) {
                    postTag.setStatus(PostPropertiesStatus.ACTIVE);
                } else {
                    postTag.setStatus(PostPropertiesStatus.INACTIVE);
                }
                postTag.setUpdateTime(new Date());
                postTag.setUpdateBy(requestDTO.getUserId());
                tagIds.add(postTag.getId().getTagId());
                postTagRepository.save(postTag);
            }
            return postTagList;
        } catch (Exception ex) {
            log.error("Error when remove post tag : {}", ex.getMessage());
            return null;
        }
    }


    @Override
    public ResponseData<List<PostResponseDTO>> getPosts() {
        try {
            List<Post> posts = postRepository.findAll();
            List<PostResponseDTO> result = posts.stream()
                    .map(this::mapToPostResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Danh sách post", result);
        } catch (Exception ex) {
            log.error("Error when get posts:{}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), ex.getMessage());
        }
    }

    @Override
    public ResponseData<PostDetailResponseDTO> getPostsById(Integer id) {
        try {
            Post posts = postRepository.findFirstById(id);
            PostDetailResponseDTO result = mapToPostDetailResponseDTO(posts);
            if (result != null) {
                return new ResponseData<>(ResponseCode.C200.getCode(), "Đã tìm thấy post với Id: " + id, result);
            }
            return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy post với Id:" + id);

        } catch (Exception ex) {
            log.error("Error when get posts with id {}:", id);
            return new ResponseData<>(ResponseCode.C207.getCode(), ex.getMessage());
        }
    }

    private PostDetailResponseDTO mapToPostDetailResponseDTO(Post post) {
        List<TypeResponseDTO> typeResponseDTOList = post.getPostTypes()
                .stream()
                .map(postType -> modelMapper.map(postType, TypeResponseDTO.class))
                .collect(Collectors.toList());

        List<TagResponseDTO> tagResponseDTOList = post.getPostTags()
                .stream()
                .map(postTag -> modelMapper.map(postTag, TagResponseDTO.class))
                .collect(Collectors.toList());

        UserInfo userInfo = userInfoRepository.findUserInfoById(post.getCreateBy());

        PostPropertiesResponseDTO postPropertiesResponseDTO = modelMapper.map(post, PostPropertiesResponseDTO.class);
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        if (postPropertiesResponseDTO.getCreate_time() != null) {
            postPropertiesResponseDTO.setCreate_time(formatter.format(post.getCreateTime()));
        }
        if (postPropertiesResponseDTO.getUpdate_time() != null) {
            postPropertiesResponseDTO.setUpdate_time(formatter.format(post.getUpdateTime()));
        }

        return PostDetailResponseDTO.builder()
                .postProperties(postPropertiesResponseDTO)
                .listType(typeResponseDTOList)
                .listTag(tagResponseDTOList)
                .create_by(mapperUserInfoResponseDTO(userInfo))
                .build();
    }

    @Override
    public ResponseData<String> updatePost(UpdatePostRequestDTO requestDTO) {
        try {
            if (requestDTO == null || requestDTO.getPostId() == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Sai request");
            }

            // Update post
            Post existingPost = postRepository.findFirstById(requestDTO.getPostId());
            if (existingPost == null) {
                throw new Exception("Không tìm thấy post");
            }
            existingPost.setTitle(requestDTO.getTitle());
            existingPost.setContent(requestDTO.getContent());
            existingPost.setUpdateTime(new Date());
            existingPost.setThumnail(requestDTO.getThumnail());
            existingPost.setQuote(requestDTO.getQuote());
            existingPost.setUrl("/" + convertURL(requestDTO.getTitle()) + "-" + randomCodeGeneratorUtil.generateRandomString());
            existingPost.setUpdateBy(requestDTO.getUpdate_by());
            postRepository.save(existingPost);

            // Update Post Types
            updatePostType(existingPost, requestDTO.getListType(), requestDTO.getUpdate_by());

            // Update Tag
            updatePostTag(existingPost, requestDTO.getListTag(), requestDTO.getUpdate_by());

            return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật post thành công");
        } catch (Exception ex) {
            log.error("Error when update post : {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), ex.getMessage());
        }
    }


    private void updatePostTag(Post existingPost, List<String> listTag, Integer updateBy) {
        // Get List Post Tag
        List<PostTag> existingPostTags = postTagRepository.findPostTagByPostId(existingPost.getId());
        List<String> listTagName = new ArrayList<>(listTag);
        // Check tag post is existed
        for (PostTag postTag : existingPostTags) {
            String existingTagName = postTag.getTag().getName();
            // Case 1: Tag not existed in db and delete it
            if (!listTag.contains(existingTagName)) {
                postTagRepository.delete(postTag);
            } else {
                // Case 2 : Tag existed in db , just update
                postTag.setUpdateTime(new Date());
                postTag.setUpdateBy(updateBy);
                postTagRepository.save(postTag);
                // Remove index of list tag
                listTagName.remove(existingTagName);
            }
        }
        // Case 1.1: Find tag if not existed in tag table , then add it
        for (String tagName : listTagName) {
            Tag tag = tagRepository.findTagByname(tagName);
            // Case 1.2:  Find tag not in db then insert tag
            if (tag == null) {
                tag = new Tag();
                tag.setName(tagName);
                tag.setCreateTime(new Date());
                tag.setCreateBy(updateBy);
                tag.setStatus(PostPropertiesStatus.ACTIVE);
                tagRepository.save(tag);
            }
            // Case 1.3: If tag existed in then insert post tag
            PostTag postTag = getPostTag(existingPost, updateBy, tag);
            postTagRepository.save(postTag);
        }
    }

    private static PostTag getPostTag(Post existingPost, Integer updateBy, Tag tag) {
        PostTagId postTagId = new PostTagId();
        postTagId.setPostId(existingPost.getId());
        postTagId.setTagId(tag.getId());
        PostTag postTag = new PostTag();
        postTag.setId(postTagId);
        postTag.setPost(existingPost);
        postTag.setTag(tag);
        postTag.setCreateTime(new Date());
        postTag.setCreateBy(updateBy);
        postTag.setStatus(PostPropertiesStatus.ACTIVE);
        return postTag;
    }

    private void updatePostType(Post existingPost, List<Integer> listType, Integer updateBy) {
        // Get List Post types
        List<PostType> existingPostTypes = postTypeRepository.findPostTypeByPostId(existingPost.getId());

        // Remove old post types not in existingPostTypes
        for (PostType postType : existingPostTypes) {
            if (!listType.contains(postType.getId().getTypeId())) {
                postTypeRepository.delete(postType);
            }
        }
        // Add or update new post types
        for (Integer typeId : listType) {
            Type type = typeRepository.findById(typeId)
                    .orElseThrow(() -> new RuntimeException("Type not found"));

            PostTypeId postTypeId = new PostTypeId(existingPost.getId(), typeId);
            PostType postType = postTypeRepository.findById(postTypeId).orElse(null);

            if (postType == null) {
                postType = new PostType();
                postType.setId(postTypeId);
                postType.setType(type);
                postType.setPost(existingPost);
                postType.setCreateTime(new Date());
                postType.setCreateBy(updateBy);
            } else {
                postType.setUpdateTime(new Date());
                postType.setUpdateBy(updateBy);
            }
            postType.setStatus(PostPropertiesStatus.ACTIVE);
            postTypeRepository.save(postType);
        }
    }

    private PostResponseDTO mapToPostResponseDTO(Post post) {
        List<TypeResponseDTO> typeResponseDTOList = post.getPostTypes()
                .stream()
                .map(postType -> modelMapper.map(postType, TypeResponseDTO.class))
                .collect(Collectors.toList());

        UserInfo userInfo = userInfoRepository.findUserInfoById(post.getCreateBy());

        PostPropertiesResponseDTO postPropertiesResponseDTO = modelMapper.map(post, PostPropertiesResponseDTO.class);
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        if (postPropertiesResponseDTO.getCreate_time() != null) {
            postPropertiesResponseDTO.setCreate_time(formatter.format(post.getCreateTime()));
        }
        if (postPropertiesResponseDTO.getUpdate_time() != null) {
            postPropertiesResponseDTO.setUpdate_time(formatter.format(post.getUpdateTime()));
        }

        return PostResponseDTO.builder()
                .postProperties(postPropertiesResponseDTO)
                .listType(typeResponseDTOList)
                .create_by(mapperUserInfoResponseDTO(userInfo))
                .build();
    }


    private List<String> validateDuplicateTag(List<String> listTag) {
        return listTag.stream()
                .map(String::trim)
                .collect(Collectors.groupingBy(tagName -> tagName, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private String convertURL(String url) {
        return url.replace(" ", "-");
    }

    private UserInfoPostResponseDTO mapperUserInfoResponseDTO(UserInfo userInfo) {
        UserInfoPostResponseDTO userInfoPostResponseDTO = new UserInfoPostResponseDTO();
        userInfoPostResponseDTO.setId(userInfo.getId());
        userInfoPostResponseDTO.setFullName(userInfo.getFirstName() + " " + userInfo.getMiddleName() + " " + userInfo.getLastName());
        userInfoPostResponseDTO.setAvatar(userInfo.getUser().getAvatar());
        return userInfoPostResponseDTO;
    }

    @Override
    public ResponseData<List<PostResponseDTO>> getPostsNewest() {
        try {
            log.info("Start retrieve post by descend create time");
            Set<String> filter = new HashSet<>();
            List<Post> post = postRepository.findPostByDescCreateTime();
            List<PostResponseDTO> postResponseDTOList = post.stream()
                    .filter(p -> filter.add(p.getId() + p.getTitle() + p.getContent()))
                    .map(this::mapToPostResponseDTO)
                    .limit(3)
                    .collect(Collectors.toList());
            log.info("End retrieve post by descend create time");
            return new ResponseData<>(ResponseCode.C200.getCode(), "Tìm thấy danh sách post", postResponseDTOList);
        } catch (Exception ex) {
            log.info("Error when get post by descend create time: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi tìm danh sách post");
        }
    }

    @Override
    public ResponseData<List<PostResponseDTO>> getPostsGeneral() {
        try {
            log.info("Start retrieve post by general");
            List<Post> post = postRepository.findPostByDescCreateTime();
            List<PostResponseDTO> postResponseDTOList = post.stream()
                    .map(this::mapToPostResponseDTO)
                    .limit(6)
                    .collect(Collectors.toList());
            Collections.shuffle(postResponseDTOList, new Random());
            log.info("End retrieve post by general");
            return new ResponseData<>(ResponseCode.C200.getCode(), "Tìm thấy danh sách post", postResponseDTOList);
        } catch (Exception ex) {
            log.info("Error when get post by descend general: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi tìm danh sách post");
        }
    }
}
