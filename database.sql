CREATE TABLE [district](
    [id]                INT IDENTITY(1,1) NOT NULL,
    [name]              NVARCHAR(50) NOT NULL,

    CONSTRAINT [district_pk] PRIMARY KEY ([id])
);
CREATE TABLE [province](
    [id]                INT IDENTITY(1,1) NOT NULL,
    [name]              NVARCHAR(50) NOT NULL,

    CONSTRAINT [province_pk] PRIMARY KEY ([id])
);
CREATE TABLE [ward](
    [id]                INT IDENTITY(1,1) NOT NULL,
    [name]              NVARCHAR(50) NOT NULL,

    CONSTRAINT [ward_pk] PRIMARY KEY ([id])
);
CREATE TABLE [province_district](
    [province_id]       INT NOT NULL,
    [district_id]       INT NOT NULL,
    [status]            VARCHAR(10) NOT NULL CHECK( status IN('ACTIVE','INACTIVE')) DEFAULT 'ACTIVE',

    CONSTRAINT province_district_pk PRIMARY KEY (province_id, district_id)
);
CREATE TABLE [district_ward](
    [district_id]       INT NOT NULL,
    [ward_id]           INT NOT NULL,

    CONSTRAINT district_ward_pk PRIMARY KEY (district_id, ward_id)
);
CREATE TABLE [address_detail](
    [id]                INT IDENTITY(1,1),
    [province_id]       INT NOT NULL FOREIGN KEY REFERENCES [province](id),
    [ward_id]           INT NOT NULL FOREIGN KEY REFERENCES [ward](id),
    [district]          INT NOT NULL FOREIGN KEY REFERENCES [district](id),
    
    CONSTRAINT [address_detail_pk] PRIMARY KEY ([id])
);
CREATE TABLE [address](
    [id]                INT IDENTITY(1,1),
    [address_detail_id] INT NOT NULL FOREIGN KEY REFERENCES [address_detail](id),

    CONSTRAINT [address_pk] PRIMARY KEY ([id])
);
CREATE TABLE [university](
    [id]                VARCHAR(20) NOT NULL,
    [name]              NVARCHAR(250) NOT NULL,
    [username]          VARCHAR(50) NOT NULL,
    [email]             VARCHAR(100) NOT NULL,
    [password]          VARCHAR(100) NOT NULL,
    [description]       NVARCHAR(300) NOT NULL,
    [address_id]        INT NOT NULL FOREIGN KEY REFERENCES [address](id)
    [avatar]            varchar(100) NOT NULL,
    [type]              VARCHAR(15) NOT NULL,
    [status]            VARCHAR(10) NOT NULL CHECK([status] IN('ACTIVE','INACTIVE','PENDING')) DEFAULT 'PENDING',

    CONSTRAINT [university_pk] PRIMARY KEY ([id])
);
CREATE TABLE [highschool](
    [id]                VARCHAR(20) NOT NULL,
    [name]              VARCHAR(250) NOT NULL,
    [username]          VARCHAR(50) NOT NULL,
    [email]             VARCHAR(100) NOT NULL,
    [password]          VARCHAR(100) NOT NULL,
    [description]       NVARCHAR(300),
    [address_id]        INT NOT NULL FOREIGN KEY REFERENCES [address](id)
    [avatar]            varchar(100) NOT NULL,
    [type]              VARCHAR(15) NOT NULL,
    [status]            VARCHAR(10) NOT NULL CHECK( status IN('ACTIVE','INACTIVE','PENDING')) DEFAULT 'PENDING',

    CONSTRAINT [highschool_pk] PRIMARY KEY ([id])
);
CREATE TABLE [staff](
    [id]                VARCHAR(20) PRIMARY KEY,
    [name]              VARCHAR(250) NOT NULL,
    [username]          VARCHAR(50) NOT NULL,
    [email]             VARCHAR(100) NOT NULL,
    [password]          VARCHAR(100) NOT NULL,
    [phone]             VARCHAR(15) NOT NULL,
    [description]       NVARCHAR(300) NOT NULL,
    [avatar]            VARCHAR(100) NOT NULL,
    [status]            VARCHAR(10) NOT NULL CHECK( status IN('ACTIVE','INACTIVE','PENDING')) DEFAULT 'PENDING',

    CONSTRAINT [staff_pk] PRIMARY KEY ([id])
);
CREATE TABLE [student](
    [id]                VARCHAR(20),
    [username]          VARCHAR(50) NOT NULL,
    [email]             VARCHAR(100) NOT NULL,
    [password]          VARCHAR(100) NOT NULL,
    [first_name]        VARCHAR(15) NOT NULL,
    [middle_name]       VARCHAR(40) NOT NULL,
    [last_name]         VARCHAR(15) NOT NULL,
    [gender]            VARCHAR() NOT NULL CHECK([gender] IN('MALE', 'FEMALE', 'OTHER')),
    [address_id]        INT NOT NULL FOREIGN KEY REFERENCES [address]([id]),
    [date_of_birth]     DATETIME NOT NULL,
    [education_level]   INT NOT NULL,
    [avatar]            VARCHAR(100) NOT NULL,
    [phone]             VARCHAR(15) NOT NULL,
    [status]            VARCHAR(10) NOT NULL CHECK([status] IN('ACTIVE', 'INACTIVE', 'PENDING')) DEFAULT 'PENDING',

    CONSTRAINT [student_pk] PRIMARY KEY ([id])
);
CREATE TABLE [admin](
    [id]                VARCHAR(20) NOT NULL,
    [username]          VARCHAR(50) NOT NULL,
    [email]             VARCHAR(100) NOT NULL,
    [password]          VARCHAR(100) NOT NULL,
    [name]              NVARCHAR(100) NOT NULL,
    [avatar]            VARCHAR(100) NOT NULL,
    [status]            VARCHAR(10) NOT NULL CHECK([status] IN('ACTIVE', 'INACTIVE', 'PENDING')) DEFAULT 'PENDING',

    CONSTRAINT [admin_pk] PRIMARY KEY ([id])
);
CREATE TABLE [consultant](
    [id]                VARCHAR(20) NOT NULL,
    [username]          VARCHAR(50) NOT NULL,
    [email]             VARCHAR(100) NOT NULL,
    [password]          VARCHAR(100) NOT NULL,
    [name]              NVARCHAR(100) NOT NULL,
    [avatar]            VARCHAR(100) NOT NULL,
    [status]            VARCHAR(10) NOT NULL CHECK([status] IN('ACTIVE', 'INACTIVE', 'PENDING')) DEFAULT 'PENDING',

    CONSTRAINT [consultant_pk] PRIMARY KEY ([id])
);
CREATE TABLE [highschool_consultant](
    [consultant_id]     VARCHAR(20) NOT NULL FOREIGN KEY REFERENCES [consultant]([id]),
    [highschool_id]     VARCHAR(20) NOT NULL FOREIGN KEY REFERENCES [highschool]([id]),

    CONSTRAINT [highschool_consultant_pk] PRIMARY KEY ([consultant_id])
);
CREATE TABLE [university_consultant](
    [consultant_id]     VARCHAR(20) NOT NULL FOREIGN KEY REFERENCES [consultant]([id]),
    [university_id]     VARCHAR(20) NOT NULL FOREIGN KEY REFERENCES [university]([id]),
    
    CONSTRAINT [university_consultant_pk] PRIMARY KEY ([consultant_id], [university_id])
)
CREATE TABLE [staff_highschool](
    [staff_id]          VARCHAR(20) NOT NULL FOREIGN KEY REFERENCES [staff]([id]),
    [highschool_id]     VARCHAR(20) NOT NULL FOREIGN KEY REFERENCES [highschool]([id]),
 
    CONSTRAINT [staff_highschool_pk] PRIMARY KEY ([staff_id], [highschool_id])
);
CREATE TABLE [staff_university](
    [staff_id]          VARCHAR(20) NOT NULL FOREIGN KEY REFERENCES [staff]([id]),
    [university_id]     VARCHAR(20) NOT NULL FOREIGN KEY REFERENCES [university]([id]),

    CONSTRAINT [staff_university_pk] PRIMARY KEY ([staff_id], [university_id])
);

=============================================================

CREATE TABLE [tag](
    [id]                INT IDENTITY(1,1) NOT NULL,
    [name]              NVARCHAR(100),

    CONSTRAINT [tag_pk] PRIMARY KEY ([id])
);
CREATE TABLE [post](
    [id]                INT IDENTITY(1,1) NOT NULL,
    [title]             NVARCHAR(150) NOT NULL,
    [content]           NVARCHAR NOT NULL,
    [thumnail_link]     VARCHAR(100) NOT NULL,
    [quote]             NVARCHAR(150),
    [create_time]       DATETIME NOT NULL,
    [modify_time]       DATETIME NOT NULL,
    [view]              INT NOT NULL DEFAULT 0,
    [status]            VARCHAR(10) NOT NULL CHECK([status] IN('ACTIVE', 'INACTIVE', 'PENDING')) DEFAULT 'PENDING',

    CONSTRAINT [post_pk] PRIMARY KEY ([id])
);
CREATE TABLE [post_tag](
    [post_id]           INT NOT NULL FOREIGN KEY REFERENCES [post]([id]),
    [tag_id]            INT NOT NULL FOREIGN KEY REFERENCES [tag]([id]),

    CONSTRAINT [post_tag_pk] PRIMARY KEY ([post_id], [tag_id])
);
CREATE TABLE [consultant_post](
    [consultant_id]     VARCHAR(20) NOT NULL FOREIGN KEY REFERENCES [consultant]([id]),
    [post_id]           INT NOT NULL FOREIGN KEY REFERENCES [post]([id]),

    CONSTRAINT [consultant_post_pk] PRIMARY KEY ([consultant_id], [post_id])
);
CREATE TABLE [staff_post](
    [staff_id]     VARCHAR(20) NOT NULL FOREIGN KEY REFERENCES [staff]([id]),
    [post_id]           INT NOT NULL FOREIGN KEY REFERENCES [post]([id]),

    CONSTRAINT [consultant_post_pk] PRIMARY KEY ([staff_id], [post_id])
);
=============================================================

CREATE TABLE [holland_test](
    [id]                INT IDENTITY(1,1) NOT NULL,
    [name]              NVARCHAR(100) NOT NULL,
    [description]       NVARCHAR(200) NOT NULL,
    [status]            VARCHAR(10) NOT NULL CHECK( status IN('ACTIVE','INACTIVE')),

    CONSTRAINT [holland_test_pk] PRIMARY KEY ([id])
);
CREATE TABLE [question](
    [id]                INT IDENTITY(1,1) NOT NULL,
    [question_text]     NVARCHAR(200) NOT NULL,
    [status]            VARCHAR(10) NOT NULL CHECK( status IN('ACTIVE','INACTIVE')),

    CONSTRAINT [question_pk] PRIMARY KEY ([id])
);
CREATE TABLE [answer](
    [id] INT IDENTITY(1,1),
    [answer_text] NVARCHAR(200) NOT NULL,
    [status] VARCHAR(10) NOT NULL CHECK( status IN('ACTIVE','INACTIVE')),

    CONSTRAINT [answer_pk] PRIMARY KEY ([id])
);
CREATE TABLE [holland_test_response](
    [id] INT IDENTITY(1,1),
    [holland_test_id] INT FOREIGN KEY REFERENCES [holland_test]([id]),
    [start_time] datetime NOT NULL,
    [end_time] datetime NOT NULL,

    CONSTRAINT [holland_test_response_pk] PRIMARY KEY ([id])
);
CREATE TABLE [holland_test_response_answer](
    [holland_test_response_id] INT FOREIGN KEY REFERENCES [holland_test]([id]),
    [answer_id] INT FOREIGN KEY REFERENCES [answer]([id]),

    CONSTRAINT ([holland_test_response_answer_pk]) PRIMARY KEY ([holland_test_response_id], [answer_id])
);
CREATE TABLE [student_holland_test_response](
    [student_id] VARCHAR(20) NOT NULL FOREIGN KEY REFERENCES [student]([id]),
    [holland_test_response_id] VARCHAR(20) NOT NULL FOREIGN KEY REFERENCES [holland_test_response]([id]),

    CONSTRAINT [student_holland_test_response_pk] PRIMARY KEY ([student_id], [holland_test_response_id])
);
CREATE TABLE [holland_test_staff](
    [staff_id] VARCHAR(20) FOREIGN KEY REFERENCES [staff]([id]),
    [holland_test_id] INT FOREIGN KEY REFERENCES [holland_test]([id]),

    CONSTRAINT ([holland_test_staff_pk]) PRIMARY KEY ([staff_id], [holland_test_id])
);
CREATE TABLE [result](
    [id] INT IDENTITY(1,1),
    [name] NVARCHAR(50) NOT NULL,
    [description] NVARCHAR(250) NOT NULL,
    [status] VARCHAR(10) NOT NULL CHECK( status IN('ACTIVE','INACTIVE')),

    CONSTRAINT [result_pk] PRIMARY KEY ([id])
);
CREATE TABLE [suggestion](
    [id] INT IDENTITY(1,1),
    [result_id] INT FOREIGN KEY REFERENCES [result]([id]),
    [value] INT NOT NULL,

    CONSTRAINT [suggestion_pk] PRIMARY KEY ([id])
);
CREATE TABLE [answer_suggestion](
    [answer_id] INT FOREIGN KEY REFERENCES [answer]([id]),
    [suggestion_id] INT FOREIGN KEY REFERENCES [suggestion]([id]),

    CONSTRAINT [answer_suggestion_pk] PRIMARY KEY ([answer_id], [suggestion_id])
);
CREATE TABLE [holland_test_question](
    [holland_test_id] INT FOREIGN KEY REFERENCES [holland_test]([id]),
    [question_id] INT FOREIGN KEY REFERENCES [question]([id]),

    CONSTRAINT [holland_test_question_pk] PRIMARY KEY ([holland_test_id], [question_id])
);
CREATE TABLE [question_answer](
    [question_id] INT FOREIGN KEY REFERENCES [question]([id]),
    [answer_id] INT FOREIGN KEY REFERENCES [answer]([id]),

    CONSTRAINT [question_answer_pk] PRIMARY KEY ([question_id], [answer_id])
);

========================================================

CREATE TABLE [ads_package](
    [id] INT IDENTITY(1,1),
    [name] NVARCHAR(100) NOT NULL,
    [description] NVARCHAR(200) NOT NULL,
    [image] VARCHAR(100) NOT NULL,
    [expected_view] INT NOT NULL,
    [price] MONEY NOT NULL CHECK([price] > 0),
    [status] VARCHAR(10) NOT NULL CHECK( status IN('ACTIVE','INACTIVE')),

    CONSTRAINT [ads_package_pk] PRIMARY KEY ([id])
);
CREATE TABLE [admin_ads_package](
    [admin_id] VAR(20) FOREIGN KEY REFERENCES [admin]([id]),
    [ads_package_id] INT FOREIGN KEY REFERENCES [ads_package]([id]),

    CONSTRAINT [admin_ads_package_pk] PRIMARY KEY ([admin_id], [ads_package_id])
);
CREATE TABLE [university_transaction](
    [id] INT IDENTITY(1,1) NOT NULL,
    [university_id] VARCHAR(20) FOREIGN KEY REFERENCES [university]([id]),
    [ads_package_id] INT FOREIGN KEY REFERENCES [ads_package]([id]),
    [create_time] DATETIME,
    [complete_time] DATETIME,
    [status] VARCHAR(10) NOT NULL CHECK([status] IN('PENDING','CANCELED', 'COMPLETED')),

    CONSTRAINT [university_transaction_pk] PRIMARY KEY ([id])
);
CREATE TABLE [university_ads_package](
    [university_id] VARCHAR(20) FOREIGN KEY REFERENCES [university]([id]),
    [university_transaction_id] INT FOREIGN KEY REFERENCES [university_transaction]([id]),
    [post_id] INT FOREIGN KEY REFERENCES [post]([id]),
    [start_view] INT NOT NULL,
    [current_view] INT NOT NULL,
    [status] VARCHAR(10) NOT NULL CHECK([status] IN('ACTIVE','COMPLETE')),

    CONSTRAINT [university_ads_package_pk] PRIMARY KEY ([university_id], [university_transaction_id])
);
CREATE TABLE [highschool_transaction](
    [id] INT IDENTITY(1,1) NOT NULL,
    [highschool_id] VARCHAR(20) FOREIGN KEY REFERENCES [highschool]([id]),
    [ads_package_id] INT FOREIGN KEY REFERENCES [ads_package]([id]),
    [create_time] DATETIME,
    [complete_time] DATETIME,
    [status] VARCHAR(10) NOT NULL CHECK([status] IN('PENDING','CANCELED', 'COMPLETED')),

    CONSTRAINT [highschool_transaction_pk] PRIMARY KEY ([id])
);
CREATE TABLE [highschool_ads_package](
    [highschool_id] VARCHAR(20) FOREIGN KEY REFERENCES [highschool]([id]),
    [highschool_transaction_id] INT FOREIGN KEY REFERENCES [highschool_transaction]([id]),
    [post_id] INT FOREIGN KEY REFERENCES [post]([id]),
    [start_view] INT NOT NULL,
    [current_view] INT NOT NULL,
    [status] VARCHAR(10) NOT NULL CHECK([status] IN('ACTIVE','COMPLETE')),

    CONSTRAINT [highschool_ads_package_pk] PRIMARY KEY ([highschool_id], [highschool_transaction_id])
);

========================================================

CREATE TABLE [subject](
    [id] INT IDENTITY(1,1) NOT NULL,
    [name] NVARCHAR(40) NOT NULL,
    [status] VARCHAR(10) NOT NULL CHECK([status] IN('ACTIVE', 'INACTIVE')) DEFAULT 'ACTIVE',

    CONSTRAINT [subject_pk] PRIMARY KEY ([id])
);
CREATE TABLE [subject_group](
    [id] INT IDENTITY(1,1) NOT NULL,
    [name] VARCHAR(3) NOT NULL,
    [status] VARCHAR(10) NOT NULL CHECK([status] IN('ACTIVE', 'INACTIVE')) DEFAULT 'ACTIVE',

    CONSTRAINT [subject_group_pk] PRIMARY KEY ([id])
);
CREATE TABLE [subject_group_subject](
    [subject_id] INT NOT NULL FOREIGN KEY REFERENCES [subject]([id]),
    [subject_group_id] INT NOT NULL FOREIGN KEY REFERENCES [subject_group]([id]),

    CONSTRAINT [subject_group_subject_pk] PRIMARY KEY ([subject_id], [subject_group_id])
);
CREATE TABLE [school_report](
    [id] INT IDENTITY(1,1) NOT NULL,
    [study_level] VARCHAR(5) NOT NULL CHECK([study_level] IN(N'THCS', N'THPT')),
    [status] VARCHAR(10) NOT NULL CHECK([status] IN('ACTIVE', 'INACTIVE')),
    
    CONSTRAINT [school_report_pk] PRIMARY KEY ([id])
);
CREATE TABLE [student_school_report](
    [student_id] VARCHAR(20) NOT NULL FOREIGN KEY REFERENCES [student]([id]),
    [school_report_id] INT NOT NULL FOREIGN REFERENCES [school_report]([id]),

    CONSTRAINT [student_school_report_pk] PRIMARY KEY ([student_id], [school_report_id])
)
CREATE TABLE [conduct_classification](
    [id] INT IDENTITY(1,1) NOT NULL,
    [grade] INT NOT NULL, 
    [type] NVARCHAR(10) NOT NULL CHECK([type] IN(N'Khá', N'Giỏi', N'Trung bình')),

    CONSTRAINT [conduct_classification_pk] PRIMARY KEY ([id])
);
CREATE TABLE [school_report_conduct_classification](
    [school_report_id] INT NOT NULL FOREIGN KEY REFERENCES [school_report]([id]),
    [conduct_classification_id] INT NOT NULL FOREIGN KEY REFERENCES [conduct_classification]([id]),

    CONSTRAINT [school_report_conduct_classification_pk] PRIMARY KEY ([school_report_id], [conduct_classification_id])
);
CREATE TABLE [grade_semester](
    [id] INT IDENTITY(1,1) NOT NULL, 
    [grade] INT NOT NULL,
    [semester] INT NOT NULL,

    CONSTRAINT [grade_semester_pk] PRIMARY KEY ([id])
);
CREATE TABLE [school_report_mark](
    [id] INT IDENTITY(1,1) NOT NULL,
    [grade_semester_id] INT NOT NULL FOREIGN KEY REFERENCES [grade_semester]([id]),
    [subject_id] INT NOT NULL FOREIGN KEY REFERENCES [subject]([id]),
    [mark] DECIMAL(2,1) NOT NULL,

    CONSTRAINT [school_report_mark_id] PRIMARY KEY ([id])
);
CREATE TABLE [school_report_school_report_mark](
    [school_report_id] INT NOT NULL FOREIGN KEY REFERENCES [school_report]([id]),
    [school_report_mark_id] INT NOT NULL FOREIGN KEY REFERENCES [school_report_mark]([id]),

    CONSTRAINT [school_report_school_report_mark_pk] PRIMARY KEY ([school_report_id], [school_report_mark_id])
);

========================================================

CREATE TABLE [major](
    [id] INT IDENTITY(1,1) PRIMARY KEY,
    [name] NVARCHAR(150) NOT NULL
);
CREATE TABLE [department](
    [id] INT IDENTITY(1,1) PRIMARY KEY,
    [name] NVARCHAR(150) NOT NULL 
);
CREATE TABLE [university_department](
    [id] INT IDENTITY(1,1) PRIMARY KEY,
    [university_id] VARCHAR(20) FOREIGN KEY REFERENCES [university]([id]),
    [department_id] INT FOREIGN KEY REFERENCES [department]([id])
);
CREATE TABLE [university_department_major](
    [id] INT IDENTITY(1,1) PRIMARY KEY,
    [university_department_id] INT FOREIGN KEY REFERENCES [university_department]([id]),
    [major_id] INT FOREIGN KEY REFERENCES [major]([id])
);
CREATE TABLE [method](
    [id] INT IDENTITY(1,1) PRIMARY KEY,
    [name] NVARCHAR(300) NOT NULL,
    [description] NVARCHAR(300) NOT NULL,
    [code] varchar(20) NOT NULL
);
CREATE TABLE [admission](
    [id] INT IDENTITY(1,1) PRIMARY KEY,
    [name] NVARCHAR(70) NOT NULL,
    [year] NVARCHAR(4) NOT NULL,
    [create_time] DATETIME NOT NULL
);
CREATE TABLE [university_admission](
    [university_id] VARCHAR(20) FOREIGN KEY REFERENCES [university]([id]),
    [admission_id] INT FOREIGN KEY REFERENCES [admission]([id]),

    CONSTRAINT [university_admission_pk] PRIMARY KEY ([university_id], [address_id])
);
CREATE TIME [highschool_admission](
    [highschool_id] VARCHAR(20) FOREIGN KEY REFERENCES [highschool]([id]),
    [admission_id] INT FOREIGN KEY REFERENCES [admission]([id]),

    CONSTRAINT [highschool_admission] PRIMARY KEY REFERENCES ([highschool_id], [admission_id])
);
CREATE TABLE [admission_method](
    [id] INT IDENTITY(1,1) PRIMARY KEY,
    [admission_id] INT FOREIGN KEY REFERENCES [admission]([id]),
    [method_id] INT FOREIGN KEY REFERENCES [method]([id]),
    [status] VARCHAR NOT NULL,
    [slot] int
);
CREATE TABLE [admission_major](
    [id] INT IDENTITY(1,1) NOT NULL,
    [university_department_major_id] INT FOREIGN KEY REFERENCES [university_department_major]([id]),
    [admission_id] INT FOREIGN KEY REFERENCES [admission]([id]),
    [license_open] ??,
    [license_open_time] DATE NOT NULL,
    [last_year_trained] BIGINT NOT NULL,
    [industry_code_conversion] VARCHAR(20),
    [industry_code_conversion_date] DATE,
    [author] NVARCHAR(20) NOT NULL,
    [year_start] INT NOT NULL,
    [year_lasted] INT NOT NULL,
    [status] VARCHAR(10) NOT NULL CHECK([status] IN('ACTIVE', 'INACTIVE')),

    CONSTRAINT [admission_major] PRIMARY KEY [id]
);
CREATE TABLE [employment_survey](
    [id] INT IDENTITY(1,1) NOT NULL,
    [degree] NVARCHAR(20) NOT NULL,
    [admission_quota] BIGINT NOT NULL,
    [admitted_student] BIGINT NOT NULL,
    [graduate_student] BIGINT NOT NULL,
    [percentage_of_graduate_have_job] DECIMAL(4,2) NOT NULL,

    CONSTRAINT [employment_survey_pk] PRIMARY KEY [id]
);
CREATE TABLE [employment_survey_major](
    [employment_survey_id] INT NOT NULL,
    [university_department_major_id] INT NOT NULL,

    CONSTRAINT [employment_survey_major_pk] PRIMARY KEY ([employment_survey_id], [university_department_major_id])
);
CREATE TABLE [employment_survey_admission](
    [employment_survey_id] INT NOT NULL FOREIGN KEY REFERENCES [employment_survey]([id]),
    [admission_id] INT NOT NULL FOREIGN KEY REFERENCES [admission]([id])

    CONSTRAINT [employment_survey_admission_pk] PRIMARY KEY ([employment_survey_id], [admission_id])
);
CREATE TABLE [verify_ticket](
    [id] INT IDENTITY(1,1),
    [staff_id] VARCHAR(20) NOT NULL FOREIGN KEY REFERENCES [staff]([id]),
    [verify_by] VARCHAR(20) NOT NULL FOREIGN KEY REFERENCES [admin]([id]),
    [create_time] DATETIME NOT NULL,
    [verify_time] DATETIME NOT NULL,
    [document] VARCHAR(100) NOT NULL,

    CONSTRAINT [verify_ticket_pk] PRIMARY KEY ([id])
)
