package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.PostPropertiesStatus;
import com.demo.admissionportal.constants.PostStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.entity.post.InfoPostResponseDTO;
import com.demo.admissionportal.dto.request.post.PostDeleteRequestDTO;
import com.demo.admissionportal.dto.request.post.PostRequestDTO;
import com.demo.admissionportal.dto.request.post.TagRequestDTO;
import com.demo.admissionportal.dto.request.post.UpdatePostRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.comment.CommentResponseDTO;
import com.demo.admissionportal.dto.response.post.*;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.entity.sub_entity.PostTag;
import com.demo.admissionportal.entity.sub_entity.PostType;
import com.demo.admissionportal.entity.sub_entity.PostView;
import com.demo.admissionportal.entity.sub_entity.id.PostTagId;
import com.demo.admissionportal.entity.sub_entity.id.PostTypeId;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.repository.sub_repository.PostTagRepository;
import com.demo.admissionportal.repository.sub_repository.PostTypeRepository;
import com.demo.admissionportal.repository.sub_repository.PostViewRepository;
import com.demo.admissionportal.service.CommentService;
import com.demo.admissionportal.service.PostService;
import com.demo.admissionportal.service.TagService;
import com.demo.admissionportal.util.impl.RandomCodeGeneratorUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
    private final UserRepository userRepository;
    private final StaffInfoRepository staffInfoRepository;
    private final ConsultantInfoRepository consultantInfoRepository;
    private final CommentService commentService;
    private final UniversityInfoRepository universityInfoRepository;
    private final UniversityCampusRepository universityCampusRepository;
    private final ProvinceRepository provinceRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseData<PostDetailResponseDTO> createPost(PostRequestDTO requestDTO) {
        Integer createBy = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        try {
            if (requestDTO == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Sai request");
            }
            // Insert Post
            Post post = postSave(requestDTO, createBy);
            if (post == null) {
                throw new Exception("Save post thất bại!");
            }

            // Insert Type Post
            List<PostType> postTypes = postTypeSave(requestDTO.getListType(), post);
            if (postTypes.isEmpty()) {
                throw new Exception("Save post type thất bại!");
            }

            // Insert Post View
            PostView postView = postViewSave(createBy, post);
            if (postView == null) {
                throw new Exception("Save post view thất bại!");
            }

            // Check Tag Name if user input duplicate
            List<String> checkTagDuplicate = validateDuplicateTag(requestDTO.getListTag());
            if (!checkTagDuplicate.isEmpty()) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Không được nhập tag trùng nhau");
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
            String info = getUserInfoPostDTO(post.getCreateBy());
            PostDetailResponseDTO responseDTO = PostDetailResponseDTO.builder()
                    .postProperties(mapperPostPropertiesResponseDTO(post))
                    .listType(mapperTypeResponseDTO(listType))
                    .listTag(mapperTagResponseDTO(listTag))
                    .create_by(info)
                    .build();

            return new ResponseData<>(ResponseCode.C200.getCode(), "Tạo post thành công", responseDTO);
        } catch (Exception ex) {
            log.error("Error when create post: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), ex.getMessage());
        }
    }


    /**
     * Mapper post properties response dto post properties response dto.
     *
     * @param post the post
     * @return the post properties response dto
     */
    public PostPropertiesResponseDTO mapperPostPropertiesResponseDTO(Post post) {
        return modelMapper.map(post, PostPropertiesResponseDTO.class);
    }


    /**
     * Mapper type response dto list.
     *
     * @param lisType the lis type
     * @return the list
     */
    public List<TypeResponseDTO> mapperTypeResponseDTO(List<Type> lisType) {
        return lisType.stream()
                .map(tag -> modelMapper.map(tag, TypeResponseDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Mapper tag response dto list.
     *
     * @param listTag the list tag
     * @return the list
     */
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

    private Post postSave(PostRequestDTO requestDTO, Integer createBy) {
        try {
            Post post = new Post();
            post.setTitle(requestDTO.getTitle());
            post.setContent(requestDTO.getContent());
            post.setQuote(requestDTO.getQuote());
            post.setThumnail(requestDTO.getThumnail());
            post.setCreateBy(createBy);
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
        Integer createBy = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        try {
            if (requestDTO == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Sai request");
            }

            // Remove post
            Post resultChangeStatusPost = changeStatusPost(requestDTO, createBy);
            if (resultChangeStatusPost == null) {
                throw new Exception("Xoá post thất bại");
            }
            // Remove post tag
            List<Integer> tagIds = new ArrayList<>();
            List<PostTag> resultChangeStatusPostTag = changeStatusPostTag(requestDTO, tagIds, createBy);
            if (resultChangeStatusPostTag == null) {
                throw new Exception("Thay đổi trạng thái  post tag thất bại");
            }
            // Remove Post Type
            boolean resultChangeStatusPostType = changeStatusPostType(requestDTO, createBy);
            if (!resultChangeStatusPostType) {
                throw new Exception("Thay đổi trạng thái  post type thất bại");
            }

            // Remove Post View
            boolean resultChangeStatusPostView = changeStatusPostView(requestDTO, createBy);
            if (!resultChangeStatusPostView) {
                throw new Exception("Thay đổi trạng thái post view thất bại");
            }
            return new ResponseData<>(ResponseCode.C200.getCode(), "Thay đổi trạng thái thành công");
        } catch (Exception ex) {
            log.error("Error when delete post: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), ex.getMessage());
        }
    }

    private boolean changeStatusPostView(PostDeleteRequestDTO requestDTO, Integer createBy) {
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
            postView.setUpdateBy(createBy);
            postViewRepository.save(postView);
            return true;
        } catch (Exception ex) {
            log.error("Error when remove post view : {}", ex.getMessage());
            return false;
        }
    }

    private boolean changeStatusPostType(PostDeleteRequestDTO requestDTO, Integer createBy) {
        try {
            List<PostType> posts = postTypeRepository.findPostTypeByPostId(requestDTO.getPostId());
            for (PostType post : posts) {
                if (post.getStatus().equals(PostPropertiesStatus.INACTIVE)) {
                    post.setStatus(PostPropertiesStatus.ACTIVE);
                } else {
                    post.setStatus(PostPropertiesStatus.INACTIVE);
                }
                post.setUpdateTime(new Date());
                post.setUpdateBy(createBy);
                postTypeRepository.save(post);
            }
            return true;
        } catch (Exception ex) {
            log.error("Error when remove post type : {}", ex.getMessage());
            return false;
        }
    }


    private Post changeStatusPost(PostDeleteRequestDTO requestDTO, Integer createBy) {
        try {
            Post post = postRepository.findFirstById(requestDTO.getPostId());
            if (post == null) {
                return null;
            }
            if (post.getStatus().equals(PostStatus.INACTIVE)) {
                post.setStatus(PostStatus.ACTIVE);
                post.setNote(requestDTO.getNote());
            } else {
                post.setStatus(PostStatus.INACTIVE);
                post.setNote(requestDTO.getNote());
            }
            post.setUpdateTime(new Date());
            post.setUpdateBy(createBy);
            return postRepository.save(post);

        } catch (Exception ex) {
            log.error("Error when remove post : {}", ex.getMessage());
            return null;
        }
    }

    private List<PostTag> changeStatusPostTag(PostDeleteRequestDTO requestDTO, List<Integer> tagIds, Integer createBy) {
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
                postTag.setUpdateBy(createBy);
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
        List<CommentResponseDTO> commentResponseDTO = commentService.getCommentFromPostId(post.getId());
        PostPropertiesResponseDTO postPropertiesResponseDTO = modelMapper.map(post, PostPropertiesResponseDTO.class);
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        if (postPropertiesResponseDTO.getCreate_time() != null) {
//            postPropertiesResponseDTO.setCreate_time(formatter.format(post.getCreateTime()));
        }
        String info = getUserInfoPostDTO(post.getCreateBy());
        return PostDetailResponseDTO.builder()
                .postProperties(postPropertiesResponseDTO)
                .listType(typeResponseDTOList)
                .listTag(tagResponseDTOList)
                .create_by(info)
                .comments(commentResponseDTO)
                .build();
    }

    @Override
    public ResponseData<String> updatePost(UpdatePostRequestDTO requestDTO) {
        Integer updateBy = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
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
            existingPost.setUpdateBy(updateBy);
            postRepository.save(existingPost);

            // Update Post Types
            updatePostType(existingPost, requestDTO.getListType(), updateBy);

            // Update Tag
            updatePostTag(existingPost, requestDTO.getListTag(), updateBy);

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
        String info = getUserInfoPostDTO(post.getCreateBy());
        PostPropertiesResponseDTO postPropertiesResponseDTO = modelMapper.map(post, PostPropertiesResponseDTO.class);
        if (postPropertiesResponseDTO.getCreate_time() != null) {
            postPropertiesResponseDTO.setCreate_time(post.getCreateTime());
        }
        return PostResponseDTO.builder()
                .postProperties(postPropertiesResponseDTO)
                .listType(typeResponseDTOList)
                .create_by(info)
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

    private String mapperStaffInfoResponseDTO(StaffInfo staffInfo) {
        return (staffInfo.getFirstName().trim() + " " + staffInfo.getMiddleName().trim() + " " + staffInfo.getLastName().trim());
    }

    private String mapperConsultantInfoResponseDTO(ConsultantInfo consultantInfo) {
        return (consultantInfo.getFirstName().trim() + " " + consultantInfo.getMiddleName().trim() + " " + consultantInfo.getLastName().trim());
    }

    @Override
    public ResponseData<List<PostResponseDTO>> getPostsNewest() {
        try {
            log.info("Start retrieve post by descend create time");
            // Filter duplicate by Id, Title, Content
            Set<String> filter = new HashSet<>();
            List<Post> post = postRepository.findPost();
            List<PostResponseDTO> postResponseDTOList = post.stream()
                    .filter(p -> filter.add(p.getId() + p.getTitle() + p.getContent()))
                    .sorted(Comparator.comparing(Post::getCreateTime).reversed())
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
            List<Post> post = postRepository.findPost();
            Set<String> filter = new HashSet<>();
            List<PostResponseDTO> postResponseDTOList = post.stream()
                    .filter(p -> filter.add(p.getId() + p.getTitle() + p.getContent()))
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

    @Override
    public ResponseData<List<PostDetailResponseDTO>> listPostByConsultOrStaffOrUniId(Integer id) {
        try {
            List<PostDetailResponseDTO> response = new ArrayList<>();
            Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            if (!Objects.equals(id, userId)) {
                return new ResponseData<>(ResponseCode.C209.getCode(), "Không đúng user");
            }
            String userRole = String.valueOf(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole());
            switch (userRole) {
                case "STAFF", "CONSULTANT":
                    response = getPostsByStaffOrConsultantId(id);
                    break;
                case "UNIVERSITY":
                    response = getPostByUniversityId(id);
                    break;
            }
            if (response == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy post với user Id: " + id);
            }
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đã tìm thấy post với user Id: " + id, response);
        } catch (Exception ex) {
            log.info("Error when list post by consult/staff/uni: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi tìm post với user Id: " + id);
        }
    }

    private List<PostDetailResponseDTO> getPostByUniversityId(Integer id) {
        List<PostDetailResponseDTO> response = null;
        try {
            List<ConsultantInfo> consultantInfos = consultantInfoRepository.findAllConsultantInfosByUniversityId(id);
            for (ConsultantInfo consultantInfo : consultantInfos) {
                List<Post> posts = postRepository.findPostByUserId(consultantInfo.getId());
                Set<String> filter = new HashSet<>();
                if (posts != null) {
                    response = posts.stream()
                            .filter(p -> filter.add(p.getId() + p.getTitle() + p.getContent()))
                            .map(this::mapToListPostDetail)
                            .collect(Collectors.toList());
                }
            }
            return response;
        } catch (Exception ex) {
            log.error("Error when get posts with university id {}:", ex.getMessage());
        }
        return response;
    }

    /**
     * Gets posts by staff or consultant id.
     *
     * @param id the id
     * @return the posts by staff or consultant id
     */
    public List<PostDetailResponseDTO> getPostsByStaffOrConsultantId(Integer id) {
        List<PostDetailResponseDTO> response = null;
        try {
            List<Post> posts = postRepository.findPostByUserId(id);
            Set<String> filter = new HashSet<>();
            if (posts != null) {
                response = posts.stream()
                        .filter(p -> filter.add(p.getId() + p.getTitle() + p.getContent()))
                        .map(this::mapToListPostDetail)
                        .collect(Collectors.toList());
            }
            return response;
        } catch (Exception ex) {
            log.error("Error when get posts with user id {}:", ex.getMessage());
        }
        return response;
    }

    /**
     * Map to list post detail post detail response dto.
     *
     * @param post the post
     * @return the post detail response dto
     */
    public PostDetailResponseDTO mapToListPostDetail(Post post) {
        List<TypeResponseDTO> typeResponseDTOList = post.getPostTypes()
                .stream()
                .map(postType -> modelMapper.map(postType, TypeResponseDTO.class))
                .collect(Collectors.toList());

        List<TagResponseDTO> tagResponseDTOList = post.getPostTags()
                .stream()
                .map(postTag -> modelMapper.map(postTag, TagResponseDTO.class))
                .collect(Collectors.toList());
        String info = getUserInfoPostDTO(post.getCreateBy());

        PostPropertiesResponseDTO postPropertiesResponseDTO = modelMapper.map(post, PostPropertiesResponseDTO.class);
        return PostDetailResponseDTO.builder()
                .postProperties(postPropertiesResponseDTO)
                .listType(typeResponseDTOList)
                .listTag(tagResponseDTOList)
                .create_by(info)
                .build();
    }

    /**
     * Gets user info post dto.
     *
     * @param createBy the create by
     * @return the user info post dto
     */
    public String getUserInfoPostDTO(Integer createBy) {
        StaffInfo staffInfo = staffInfoRepository.findStaffInfoById(createBy);
        ConsultantInfo consultantInfo = consultantInfoRepository.findConsultantInfoById(createBy);
        String userInfo = null;
        if (staffInfo != null) {
            userInfo = mapperStaffInfoResponseDTO(staffInfo);
        } else if (consultantInfo != null) {
            userInfo = mapperConsultantInfoResponseDTO(consultantInfo);
        }
        return userInfo;
    }


    @Override
    public ResponseData<Page<PostDetailResponseDTOV2>> listAllPostConsulOrStaff(Pageable pageable) {
        try {
            List<PostDetailResponseDTOV2> response = new ArrayList<>();
            Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            String userRole = String.valueOf(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole());
            switch (userRole) {
                case "STAFF":
                    response = getAllPostByStaff();
                    break;
                case "CONSULTANT":
                    response = getAllPostByConsultantId(userId);
                    break;
                case "UNIVERSITY":
                    response = getPostByUniversityIdV2(userId);
                    break;
            }
            if (response == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy post");
            }
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), response.size());
            List<PostDetailResponseDTOV2> pagedResponse = response.subList(start, end);

            Page<PostDetailResponseDTOV2> postDetailResponseDTOV2Page = new PageImpl<>(pagedResponse, pageable, response.size());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đã tìm thấy post: ", postDetailResponseDTOV2Page);
        } catch (Exception ex) {
            log.info("Error when list all post by consult/staff/uni: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi tìm post", null);
        }
    }

    private List<PostDetailResponseDTOV2> getAllPostByConsultantId(Integer id) {
        ConsultantInfo consultantInfo = consultantInfoRepository.findConsultantInfoById(id);
        return getPostByUniversityIdV2(consultantInfo.getUniversityId());
    }

    private List<PostDetailResponseDTOV2> getAllPostByStaff() {
        List<StaffInfo> staffInfo = staffInfoRepository.findAll();
        return staffInfo.stream()
                .flatMap(staff -> getPostsByStaffOrConsultantIdV2(staff.getId()).stream())
                .sorted(Comparator.comparing(PostDetailResponseDTOV2::getId))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseData<List<PostFavoriteResponseDTO>> listPostFavorite() {
        try {
            log.info("Start retrieve post favorite");
            // Filter duplicate by id, Title, Content
            List<Post> post = postRepository.findAll();
            List<PostFavoriteResponseDTO> postResponseDTOList = post.stream()
                    .sorted(Comparator.comparing(Post::getCreateTime).reversed())
                    .map(this::mapToPostFavoriteDTO)
                    .peek(this::filterPost)
                    .filter(this::filterRecentPosts)
                    .collect(Collectors.toList());

            /* Group by day and get the highest interaction post for each day
                Example: KEY: 2024-01-21    VALUES
                              ----//----    2127
                              ----//----    2128
                              ----//----    2129
                 We need to calculator interact post and get highest in each day by key
            */
            // Group by day and get the highest interaction post for each day
            Map<Date, PostFavoriteResponseDTO> highestInteractionByDay = postResponseDTOList.stream()
                    .collect(Collectors.groupingBy(
                            postFavoriteResponseDTO -> {
                                LocalDate localDate = postFavoriteResponseDTO.getPostProperties().getCreate_time().toInstant()
                                        .atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDate();
                                return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                            },
                            Collectors.collectingAndThen(
                                    Collectors.maxBy(Comparator.comparing(PostFavoriteResponseDTO::getInteractPost)),
                                    Optional::get
                            )
                    ));
            // Collect results and display most recent create_date
            List<PostFavoriteResponseDTO> resultOfFilter = new ArrayList<>(highestInteractionByDay.values())
                    .stream()
                    .sorted(Comparator.comparing(PostFavoriteResponseDTO::getPublishAgo).reversed())
                    .toList();

            log.info("End retrieve post favorite");
            return new ResponseData<>(ResponseCode.C200.getCode(), "Tìm thấy danh sách post favorite", resultOfFilter);
        } catch (Exception ex) {
            log.info("Error when get post favorite: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi tìm danh sách post favorite");
        }
    }

    private boolean filterRecentPosts(PostFavoriteResponseDTO postFavoriteResponseDTO) {
        ZoneId vietnamZone = ZoneId.of("Asia/Ho_Chi_Minh");
        Date datePost = postFavoriteResponseDTO.getPostProperties().getCreate_time();
        Date dateNow = new Date();
        LocalDateTime localDateTimePost = datePost.toInstant().atZone(vietnamZone).toLocalDateTime();
        LocalDateTime localDateTimeNow = dateNow.toInstant().atZone(vietnamZone).toLocalDateTime();
        long dateAgo = ChronoUnit.DAYS.between(localDateTimePost.toLocalDate(), localDateTimeNow.toLocalDate());
        return dateAgo >= 0 && dateAgo <= 3;
    }

    private void filterPost(PostFavoriteResponseDTO postFavoriteResponseDTO) {
        // Count comment in post
        int countComment = commentRepository.countByPostId(postFavoriteResponseDTO.getPostProperties().getId());
        // Get view in post
        int view = postFavoriteResponseDTO.getPostProperties().getView();
        // Get like in post
        int like = postFavoriteResponseDTO.getPostProperties().getLike();
        // Average contact with post
        float interactPost = (float) (countComment + view + like) / 3;
        postFavoriteResponseDTO.setInteractPost(interactPost);
    }

    /**
     * Map to post favorite dto post favorite response dto.
     *
     * @param post the post
     * @return the post favorite response dto
     */
    public PostFavoriteResponseDTO mapToPostFavoriteDTO(Post post) {
        try {
            ConsultantInfo consultantInfo = consultantInfoRepository.findConsultantInfoById(post.getCreateBy());
            StaffInfo staffInfo = staffInfoRepository.findStaffInfoById(post.getCreateBy());
            String fullName = null;
            String location = null;
            Integer id = 0;
            // Case 1 : find post by staff
            if (staffInfo != null && consultantInfo == null) {
                Province province = provinceRepository.findProvinceById(staffInfo.getProvinceId());
                id = staffInfo.getId();
                fullName = staffInfo.getFirstName() + " " + staffInfo.getMiddleName() + " " + staffInfo.getLastName();
                location = province.getName();
            } else if (consultantInfo != null && staffInfo == null) {
                // Case 2 : if consultant is existed and find it
                User user = userRepository.findUserById(consultantInfo.getUniversityId());
                UniversityInfo universityInfo = universityInfoRepository.findUniversityInfoById(user.getId());
                UniversityCampus universityCampus = universityCampusRepository.findUniversityCampusByUniversityId(universityInfo.getId());
                id = universityInfo.getId();
                fullName = universityInfo.getName() + " " + universityCampus.getCampusName();
                location = universityCampus.getSpecificAddress();
            }

            PostPropertiesResponseDTO postPropertiesResponseDTO = modelMapper.map(post, PostPropertiesResponseDTO.class);
            return PostFavoriteResponseDTO.builder()
                    .postProperties(postPropertiesResponseDTO)
                    .info(InfoPostResponseDTO.builder()
                            .id(id)
                            .name(fullName)
                            .location(location)
                            .build())
                    .publishAgo(postPropertiesResponseDTO.getCreate_time())
                    .build();
        } catch (Exception ex) {
            log.info("Error when map post favorite: {}", ex.getMessage());
            return null;
        }
    }

    /**
     * Gets posts by staff or consultant id v 2.
     *
     * @param id the id
     * @return the posts by staff or consultant id v 2
     */
    public List<PostDetailResponseDTOV2> getPostsByStaffOrConsultantIdV2(Integer id) {
        List<PostDetailResponseDTOV2> response = null;
        try {
            List<Post> posts = postRepository.findPostByUserId(id);
            Set<String> filter = new HashSet<>();
            if (posts != null) {
                response = posts.stream()
                        .filter(p -> filter.add(p.getId() + p.getTitle() + p.getContent()))
                        .map(this::mapToListPostDetailV2)
                        .collect(Collectors.toList());
            }
            return response;
        } catch (Exception ex) {
            log.error("Error when get posts version 2 with user id {}:", ex.getMessage());
        }
        return response;
    }

    /**
     * Map to list post detail v 2 post detail response dtov 2.
     *
     * @param post the post
     * @return the post detail response dtov 2
     */
    public PostDetailResponseDTOV2 mapToListPostDetailV2(Post post) {
        List<TypeResponseDTO> typeResponseDTOList = post.getPostTypes()
                .stream()
                .map(postType -> modelMapper.map(postType, TypeResponseDTO.class))
                .collect(Collectors.toList());
        String listType = typeResponseDTOList.stream()
                .map(type -> String.valueOf(type.getName()))
                .collect(Collectors.joining(","));
        String info = getUserInfoPostDTO(post.getCreateBy());

        PostDetailResponseDTOV2 postPropertiesResponseDTO = modelMapper.map(post, PostDetailResponseDTOV2.class);

        return PostDetailResponseDTOV2.builder()
                .id(postPropertiesResponseDTO.getId())
                .title(postPropertiesResponseDTO.getTitle())
                .createBy(info.trim())
                .createTime(post.getCreateTime())
                .status(postPropertiesResponseDTO.getStatus())
                .type(listType)
                .url(postPropertiesResponseDTO.getUrl())
                .note(postPropertiesResponseDTO.getNote())
                .build();
    }

    private List<PostDetailResponseDTOV2> getPostByUniversityIdV2(Integer id) {
        List<PostDetailResponseDTOV2> response = null;
        try {
            List<ConsultantInfo> consultantInfos = consultantInfoRepository.findAllConsultantInfosByUniversityId(id);
            for (ConsultantInfo consultantInfo : consultantInfos) {
                List<Post> posts = postRepository.findPostByUserId(consultantInfo.getId());
                Set<String> filter = new HashSet<>();
                if (posts != null) {
                    response = posts.stream()
                            .filter(p -> filter.add(p.getId() + p.getTitle() + p.getContent()))
                            .map(this::mapToListPostDetailV2)
                            .sorted(Comparator.comparing(PostDetailResponseDTOV2::getId))
                            .collect(Collectors.toList());
                }
            }
            return response;
        } catch (Exception ex) {
            log.error("Error when get posts version 2 with university id {}:", ex.getMessage());
        }
        return response;
    }
}
