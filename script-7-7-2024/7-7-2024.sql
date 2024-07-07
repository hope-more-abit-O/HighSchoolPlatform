USE [master]
GO
/****** Object:  Database [UAP_DEV]    Script Date: 7/7/2024 2:25:13 AM ******/
CREATE DATABASE [UAP_DEV]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'UAP_DEV', FILENAME = N'/var/opt/mssql/data/UAP_DEV.mdf' , SIZE = 73728KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'UAP_DEV_log', FILENAME = N'/var/opt/mssql/data/UAP_DEV_log.ldf' , SIZE = 73728KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [UAP_DEV] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [UAP_DEV].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [UAP_DEV] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [UAP_DEV] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [UAP_DEV] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [UAP_DEV] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [UAP_DEV] SET ARITHABORT OFF 
GO
ALTER DATABASE [UAP_DEV] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [UAP_DEV] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [UAP_DEV] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [UAP_DEV] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [UAP_DEV] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [UAP_DEV] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [UAP_DEV] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [UAP_DEV] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [UAP_DEV] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [UAP_DEV] SET  ENABLE_BROKER 
GO
ALTER DATABASE [UAP_DEV] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [UAP_DEV] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [UAP_DEV] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [UAP_DEV] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [UAP_DEV] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [UAP_DEV] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [UAP_DEV] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [UAP_DEV] SET RECOVERY FULL 
GO
ALTER DATABASE [UAP_DEV] SET  MULTI_USER 
GO
ALTER DATABASE [UAP_DEV] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [UAP_DEV] SET DB_CHAINING OFF 
GO
ALTER DATABASE [UAP_DEV] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [UAP_DEV] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [UAP_DEV] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [UAP_DEV] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'UAP_DEV', N'ON'
GO
ALTER DATABASE [UAP_DEV] SET QUERY_STORE = ON
GO
ALTER DATABASE [UAP_DEV] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [UAP_DEV]
GO
/****** Object:  Table [dbo].[address]    Script Date: 7/7/2024 2:25:14 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[address](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[address_detail_id] [int] NOT NULL,
	[specific_address] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[address_detail]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[address_detail](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[district_id] [int] NOT NULL,
	[province_id] [int] NOT NULL,
	[ward_id] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[admin]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[admin](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[avatar] [varchar](255) NOT NULL,
	[email] [varchar](255) NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[password] [varchar](255) NOT NULL,
	[phone] [varchar](255) NOT NULL,
	[role] [varchar](255) NULL,
	[status] [nvarchar](255) NOT NULL,
	[username] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[admin_info]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[admin_info](
	[admin_id] [int] NOT NULL,
	[first_name] [nvarchar](30) NULL,
	[middle_name] [nvarchar](30) NULL,
	[last_name] [nvarchar](30) NULL,
	[phone] [varchar](max) NOT NULL,
 CONSTRAINT [PK__admin_in__43AA4141EA47A844] PRIMARY KEY CLUSTERED 
(
	[admin_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[admin_token]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[admin_token](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[admin_token] [varchar](255) NOT NULL,
	[expired] [bit] NOT NULL,
	[refresh_token_admin_token] [varchar](255) NOT NULL,
	[revoked] [bit] NOT NULL,
	[token_type] [varchar](255) NOT NULL,
	[admin_id] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[admission]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[admission](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[university_id] [int] NOT NULL,
	[create_by_staff_id] [int] NOT NULL,
	[name] [nvarchar](50) NOT NULL,
	[year] [varchar](4) NOT NULL,
	[source] [nvarchar](200) NULL,
	[create_time] [datetime] NOT NULL,
	[create_by] [int] NULL,
	[update_time] [datetime] NULL,
	[update_by] [int] NULL,
	[status] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[admission_method]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[admission_method](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[method_id] [int] NOT NULL,
	[admission_id] [int] NOT NULL,
	[quota] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[admission_training_program]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[admission_training_program](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[major_id] [int] NOT NULL,
	[admission_id] [int] NOT NULL,
	[main_subject_id] [int] NOT NULL,
	[language] [varchar](255) NULL,
	[training_specific] [varchar](255) NULL,
	[quota] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[admission_training_program_method]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[admission_training_program_method](
	[admission_method_id] [int] NOT NULL,
	[admission_training_program_id] [int] NOT NULL,
	[quota] [int] NULL,
	[addmission_score] [float] NULL,
PRIMARY KEY CLUSTERED 
(
	[admission_training_program_id] ASC,
	[admission_method_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[admission_training_program_subject_group]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[admission_training_program_subject_group](
	[admission_training_program_id] [int] NOT NULL,
	[subject_group_id] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[admission_training_program_id] ASC,
	[subject_group_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ads_package]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ads_package](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](30) NOT NULL,
	[description] [nvarchar](100) NOT NULL,
	[image] [varchar](20) NOT NULL,
	[view_boost_value] [int] NOT NULL,
	[price] [float] NOT NULL,
	[create_by] [int] NOT NULL,
	[create_time] [datetime] NOT NULL,
	[update_time] [datetime] NULL,
	[update_by] [int] NULL,
	[status] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[comment]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[comment](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[post_id] [int] NOT NULL,
	[comment_parent_id] [int] NULL,
	[commenter_id] [int] NOT NULL,
	[content] [nvarchar](700) NULL,
	[create_time] [datetime] NOT NULL,
	[update_time] [datetime] NULL,
	[comment_type] [nvarchar](255) NOT NULL,
	[comment_status] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[comment_report_detail]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[comment_report_detail](
	[comment_id] [int] NOT NULL,
	[report_id] [int] IDENTITY(1,1) NOT NULL,
	[content] [nvarchar](255) NULL,
	[report_action] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[report_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[consultant]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[consultant](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[avatar] [varchar](255) NULL,
	[email] [varchar](255) NULL,
	[name] [varchar](255) NULL,
	[password] [varchar](255) NULL,
	[phone] [varchar](255) NULL,
	[reset_pass_token] [varchar](255) NULL,
	[role] [varchar](255) NULL,
	[status] [varchar](255) NOT NULL,
	[username] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[consultant_info]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[consultant_info](
	[consultant_id] [int] NOT NULL,
	[university_id] [int] NOT NULL,
	[middle_name] [nvarchar](30) NOT NULL,
	[last_name] [nvarchar](30) NOT NULL,
	[phone] [varchar](11) NULL,
	[specific_address] [nvarchar](255) NOT NULL,
	[district_id] [int] NOT NULL,
	[province_id] [int] NOT NULL,
	[ward_id] [int] NOT NULL,
	[gender] [nvarchar](255) NOT NULL,
	[first_name] [nvarchar](30) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[consultant_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[consultant_token]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[consultant_token](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[consultant_token] [varchar](255) NOT NULL,
	[expired] [bit] NOT NULL,
	[refresh_token_consultant_token] [varchar](255) NOT NULL,
	[revoked] [bit] NOT NULL,
	[token_type] [varchar](255) NOT NULL,
	[consultant_id] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[create_university_request]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[create_university_request](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[university_name] [nvarchar](250) NULL,
	[university_code] [varchar](10) NULL,
	[university_email] [varchar](250) NULL,
	[note] [nvarchar](700) NULL,
	[documents] [varchar](500) NULL,
	[create_time] [datetime] NOT NULL,
	[create_by] [int] NULL,
	[update_time] [datetime] NULL,
	[update_by] [int] NULL,
	[confirm_by] [int] NULL,
	[university_type] [varchar](30) NULL,
	[status] [nvarchar](255) NOT NULL,
	[university_username] [varchar](30) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[create_university_ticket]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[create_university_ticket](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[documents] [varchar](255) NOT NULL,
	[university_code] [varchar](255) NOT NULL,
	[university_name] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[district]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[district](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](50) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[district_ward]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[district_ward](
	[district_id] [int] NOT NULL,
	[ward_id] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[district_id] ASC,
	[ward_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[function_report]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[function_report](
	[report_id] [int] IDENTITY(1,1) NOT NULL,
	[content] [nvarchar](255) NULL,
	[proofs] [varchar](500) NULL,
	[report_action] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[report_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[job]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[job](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[create_by] [int] NOT NULL,
	[create_time] [datetime] NOT NULL,
	[update_time] [datetime] NULL,
	[update_by] [int] NULL,
	[status] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[major]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[major](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[code] [varchar](20) NOT NULL,
	[name] [nvarchar](300) NOT NULL,
	[note] [nvarchar](300) NULL,
	[create_time] [datetime] NOT NULL,
	[update_time] [datetime] NULL,
	[status] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[method]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[method](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[code] [varchar](20) NOT NULL,
	[name] [nvarchar](300) NOT NULL,
	[create_time] [datetime] NOT NULL,
	[create_by] [int] NULL,
	[update_time] [datetime] NULL,
	[update_by] [int] NULL,
	[status] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[post]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[post](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[title] [nvarchar](100) NOT NULL,
	[content] [nvarchar](max) NULL,
	[thumnail] [varchar](100) NULL,
	[quote] [varchar](100) NULL,
	[view] [int] NULL,
	[like] [int] NULL,
	[status] [nvarchar](255) NOT NULL,
	[create_time] [datetime] NOT NULL,
	[create_by] [int] NOT NULL,
	[update_time] [datetime] NULL,
	[update_by] [int] NULL,
	[quota] [varchar](255) NULL,
	[user_id] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[post_report]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[post_report](
	[report_id] [int] IDENTITY(1,1) NOT NULL,
	[post_id] [int] NOT NULL,
	[report_action] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[report_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[post_tag]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[post_tag](
	[tag_id] [int] NOT NULL,
	[post_id] [int] NOT NULL,
	[create_time] [datetimeoffset](6) NULL,
	[create_by] [int] NULL,
	[update_time] [datetimeoffset](6) NULL,
	[update_by] [int] NULL,
	[status] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[post_id] ASC,
	[tag_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[post_type]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[post_type](
	[type_id] [int] NOT NULL,
	[post_id] [int] NOT NULL,
	[create_time] [datetimeoffset](6) NULL,
	[create_by] [int] NULL,
	[update_time] [datetimeoffset](6) NULL,
	[update_by] [int] NULL,
	[status] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[post_id] ASC,
	[type_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[post_view]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[post_view](
	[post_id] [int] NOT NULL,
	[create_time] [datetime] NOT NULL,
	[view_count] [int] NULL,
	[like_count] [int] NULL,
	[create_by] [int] NULL,
	[update_time] [datetimeoffset](6) NULL,
	[update_by] [int] NULL,
	[status] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[post_id] ASC,
	[create_time] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[province]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[province](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](50) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[province_district]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[province_district](
	[province_id] [int] NOT NULL,
	[district_id] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[province_id] ASC,
	[district_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[question]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[question](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[content] [nvarchar](100) NOT NULL,
	[create_by] [int] NOT NULL,
	[create_time] [datetime] NOT NULL,
	[update_time] [datetime] NULL,
	[update_by] [int] NULL,
	[type] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[question_job]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[question_job](
	[job_id] [int] NOT NULL,
	[question_id] [int] NOT NULL,
	[create_by] [int] NOT NULL,
	[create_time] [datetime] NOT NULL,
	[update_time] [datetime] NULL,
	[update_by] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[job_id] ASC,
	[question_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[question_question_type]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[question_question_type](
	[question_id] [int] NOT NULL,
	[question_type_id] [int] NOT NULL,
	[create_by] [int] NOT NULL,
	[create_time] [datetime] NOT NULL,
	[update_time] [datetime] NULL,
	[update_by] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[question_type_id] ASC,
	[question_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[question_type]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[question_type](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[create_by] [int] NOT NULL,
	[create_time] [datetime] NOT NULL,
	[update_time] [datetime] NULL,
	[update_by] [int] NULL,
	[status] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[questionnaire]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[questionnaire](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](100) NOT NULL,
	[description] [nvarchar](100) NOT NULL,
	[cover_image] [varchar](150) NULL,
	[create_by] [int] NOT NULL,
	[create_time] [datetime] NOT NULL,
	[update_time] [datetime] NULL,
	[update_by] [int] NULL,
	[status] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[questionnaire_question]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[questionnaire_question](
	[questionnaire_id] [int] NOT NULL,
	[question_id] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[questionnaire_id] ASC,
	[question_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[report]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[report](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[create_by] [int] NOT NULL,
	[create_time] [datetime] NOT NULL,
	[update_time] [datetime] NULL,
	[update_by] [int] NULL,
	[complete_time] [datetime] NULL,
	[complete_by] [int] NULL,
	[content] [nvarchar](255) NULL,
	[response] [nvarchar](500) NULL,
	[report_type] [nvarchar](255) NOT NULL,
	[status] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[staff]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[staff](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[avatar] [varchar](255) NOT NULL,
	[email] [varchar](255) NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[password] [varchar](255) NOT NULL,
	[phone] [varchar](255) NOT NULL,
	[reset_pass_token] [varchar](255) NULL,
	[role] [varchar](255) NULL,
	[status] [nvarchar](255) NOT NULL,
	[username] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[staff_info]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[staff_info](
	[staff_id] [int] NOT NULL,
	[admin_id] [int] NOT NULL,
	[first_name] [nvarchar](30) NOT NULL,
	[middle_name] [nvarchar](30) NOT NULL,
	[last_name] [nvarchar](30) NOT NULL,
	[phone] [varchar](30) NOT NULL,
 CONSTRAINT [PK__staff_in__1963DD9CAE68BC15] PRIMARY KEY CLUSTERED 
(
	[staff_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[staff_token]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[staff_token](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[expired] [bit] NOT NULL,
	[refresh_token_staff_token] [varchar](255) NOT NULL,
	[revoked] [bit] NOT NULL,
	[staff_token] [varchar](255) NOT NULL,
	[token_type] [varchar](255) NOT NULL,
	[staff_id] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[staff_university]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[staff_university](
	[staff_id] [int] NOT NULL,
	[university_id] [int] NOT NULL,
	[create_time] [datetime2](6) NULL,
PRIMARY KEY CLUSTERED 
(
	[staff_id] ASC,
	[university_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[student_report]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[student_report](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[user_id] [int] NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[create_by] [int] NULL,
	[update_time] [datetime] NULL,
	[update_by] [int] NULL,
	[confirm_by] [int] NOT NULL,
	[status] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[student_report_mark]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[student_report_mark](
	[student_report_id] [int] NOT NULL,
	[subject_grade_semester_id] [int] NOT NULL,
	[mark] [float] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[student_report_id] ASC,
	[subject_grade_semester_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[student_token]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[student_token](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[expired] [bit] NOT NULL,
	[refresh_token_student] [varchar](255) NOT NULL,
	[revoked] [bit] NOT NULL,
	[student_token] [varchar](255) NOT NULL,
	[token_type] [varchar](255) NOT NULL,
	[student_id] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[subject]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[subject](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](40) NOT NULL,
	[create_time] [datetime] NOT NULL,
	[create_by] [int] NULL,
	[update_time] [datetime] NULL,
	[update_by] [int] NULL,
	[status] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[subject_grade_semester]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[subject_grade_semester](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[subject_id] [int] NOT NULL,
	[grade] [varchar](2) NOT NULL,
	[semester] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[subject_group]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[subject_group](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [varchar](3) NOT NULL,
	[create_time] [datetime] NOT NULL,
	[create_by] [int] NULL,
	[update_time] [datetime] NULL,
	[update_by] [int] NULL,
	[status] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[subject_group_subject]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[subject_group_subject](
	[subject_group_id] [int] NOT NULL,
	[subject_id] [int] NOT NULL,
	[create_time] [datetime] NOT NULL,
	[create_by] [int] NULL,
	[update_time] [datetime] NULL,
	[update_by] [int] NULL,
	[status] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[subject_id] ASC,
	[subject_group_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[tag]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tag](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[create_time] [datetime] NOT NULL,
	[create_by] [int] NULL,
	[update_time] [datetime] NULL,
	[update_by] [int] NULL,
	[status] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[test_response]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[test_response](
	[id] [int] NOT NULL,
	[questionnaire_id] [int] NOT NULL,
	[create_time] [datetime] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[test_response_answer]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[test_response_answer](
	[question_id] [int] NOT NULL,
	[test_response_id] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[test_response_id] ASC,
	[question_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[type]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[type](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[create_time] [datetimeoffset](6) NULL,
	[create_by] [int] NULL,
	[update_time] [datetimeoffset](6) NULL,
	[update_by] [int] NULL,
	[status] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[university]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[university](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[address_id] [int] NULL,
	[avatar] [varchar](255) NOT NULL,
	[code] [varchar](255) NULL,
	[description] [varchar](255) NOT NULL,
	[email] [varchar](255) NULL,
	[name] [varchar](255) NOT NULL,
	[password] [varchar](255) NOT NULL,
	[phone] [varchar](255) NOT NULL,
	[reset_pass_token] [varchar](255) NULL,
	[role] [varchar](255) NULL,
	[status] [nvarchar](255) NOT NULL,
	[type] [varchar](255) NOT NULL,
	[username] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[university_campus]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[university_campus](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[university_id] [int] NOT NULL,
	[campus_name] [nvarchar](255) NOT NULL,
	[email] [varchar](100) NULL,
	[specific_address] [nvarchar](255) NOT NULL,
	[province_id] [int] NOT NULL,
	[district_id] [int] NOT NULL,
	[ward_id] [int] NOT NULL,
	[phone] [varchar](12) NULL,
	[picture] [varchar](300) NULL,
	[create_time] [datetime] NOT NULL,
	[create_by] [int] NULL,
	[update_time] [datetime] NULL,
	[update_by] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[university_consultant]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[university_consultant](
	[consultant_id] [int] NOT NULL,
	[university_id] [int] NOT NULL,
	[create_time] [datetime2](6) NULL,
PRIMARY KEY CLUSTERED 
(
	[consultant_id] ASC,
	[university_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[university_info]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[university_info](
	[university_id] [int] NOT NULL,
	[create_university_request_id] [int] NOT NULL,
	[code] [varchar](255) NOT NULL,
	[university_username] [varchar](30) NULL,
	[name] [nvarchar](255) NOT NULL,
	[description] [nvarchar](max) NOT NULL,
	[university_type] [varchar](30) NULL,
	[cover_image] [varchar](100) NULL,
	[type] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[university_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[university_package]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[university_package](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[university_id] [int] NOT NULL,
	[university_transaction_id] [int] NOT NULL,
	[post_id] [int] NULL,
	[start_view] [int] NULL,
	[end_view] [int] NULL,
	[create_time] [datetime] NOT NULL,
	[complete_time] [datetime] NULL,
	[status] [nvarchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[university_token]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[university_token](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[expired] [bit] NOT NULL,
	[refresh_token_university_token] [varchar](255) NOT NULL,
	[revoked] [bit] NOT NULL,
	[token_type] [varchar](255) NOT NULL,
	[university_token] [varchar](255) NOT NULL,
	[university_id] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[university_transaction]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[university_transaction](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[university_id] [int] NOT NULL,
	[ads_package_id] [int] NOT NULL,
	[create_by] [int] NOT NULL,
	[create_time] [datetime] NOT NULL,
	[update_time] [datetime] NULL,
	[update_by] [int] NULL,
	[complete_time] [datetime] NULL,
	[status] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[university_view]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[university_view](
	[university_id] [int] NOT NULL,
	[saved_time] [datetime] NOT NULL,
	[view_count] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[university_id] ASC,
	[saved_time] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[user]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[user](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[username] [varchar](30) NOT NULL,
	[email] [varchar](30) NOT NULL,
	[password] [varchar](255) NOT NULL,
	[note] [nvarchar](255) NULL,
	[avatar] [varchar](20) NOT NULL,
	[role] [varchar](20) NOT NULL,
	[status] [nvarchar](255) NOT NULL,
	[create_time] [datetime] NOT NULL,
	[create_by] [int] NULL,
	[update_time] [datetime] NULL,
	[update_by] [int] NULL,
	[provider] [varchar](255) NULL,
	[provider_id] [varchar](255) NULL,
	[reset_pass_token] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[user_info]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[user_info](
	[user_id] [int] NOT NULL,
	[first_name] [nvarchar](30) NOT NULL,
	[middle_name] [nvarchar](30) NOT NULL,
	[last_name] [nvarchar](30) NOT NULL,
	[phone] [varchar](11) NULL,
	[birthday] [date] NULL,
	[gender] [nvarchar](255) NOT NULL,
	[specific_address] [nvarchar](255) NOT NULL,
	[district_id] [int] NOT NULL,
	[province_id] [int] NOT NULL,
	[ward_id] [int] NOT NULL,
	[education_level] [nvarchar](255) NOT NULL,
 CONSTRAINT [PK__user_inf__B9BE370FA73764F1] PRIMARY KEY CLUSTERED 
(
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[user_token]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[user_token](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[user_id] [int] NOT NULL,
	[token] [varchar](255) NOT NULL,
	[token_type] [varchar](50) NOT NULL,
	[expired] [bit] NULL,
	[revoked] [bit] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC,
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ward]    Script Date: 7/7/2024 2:25:15 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ward](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](50) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Index [UK_kpfnivid38f5bwx3yl1lxeeae]    Script Date: 7/7/2024 2:25:15 AM ******/
CREATE UNIQUE NONCLUSTERED INDEX [UK_kpfnivid38f5bwx3yl1lxeeae] ON [dbo].[post]
(
	[user_id] ASC
)
WHERE ([user_id] IS NOT NULL)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[admin] ADD  DEFAULT ('ACTIVE') FOR [status]
GO
ALTER TABLE [dbo].[admission] ADD  DEFAULT ((0)) FOR [create_by]
GO
ALTER TABLE [dbo].[admission] ADD  DEFAULT (NULL) FOR [update_by]
GO
ALTER TABLE [dbo].[admission] ADD  DEFAULT ('PENDING') FOR [status]
GO
ALTER TABLE [dbo].[ads_package] ADD  DEFAULT ('ACTIVE') FOR [status]
GO
ALTER TABLE [dbo].[comment] ADD  DEFAULT ('ROOT') FOR [comment_type]
GO
ALTER TABLE [dbo].[comment] ADD  DEFAULT ('ACTIVE') FOR [comment_status]
GO
ALTER TABLE [dbo].[comment_report_detail] ADD  DEFAULT ('UNPROCESS') FOR [report_action]
GO
ALTER TABLE [dbo].[consultant_info] ADD  DEFAULT ('MALE') FOR [gender]
GO
ALTER TABLE [dbo].[create_university_request] ADD  DEFAULT ((0)) FOR [create_by]
GO
ALTER TABLE [dbo].[create_university_request] ADD  DEFAULT (NULL) FOR [update_by]
GO
ALTER TABLE [dbo].[create_university_request] ADD  DEFAULT ('PENDING') FOR [status]
GO
ALTER TABLE [dbo].[function_report] ADD  DEFAULT ('UNPROCESS') FOR [report_action]
GO
ALTER TABLE [dbo].[job] ADD  DEFAULT ('ACTIVE') FOR [status]
GO
ALTER TABLE [dbo].[major] ADD  DEFAULT ('ACTIVE') FOR [status]
GO
ALTER TABLE [dbo].[method] ADD  DEFAULT ((0)) FOR [create_by]
GO
ALTER TABLE [dbo].[method] ADD  DEFAULT (NULL) FOR [update_by]
GO
ALTER TABLE [dbo].[method] ADD  DEFAULT ('ACTIVE') FOR [status]
GO
ALTER TABLE [dbo].[post] ADD  DEFAULT ((0)) FOR [view]
GO
ALTER TABLE [dbo].[post] ADD  DEFAULT ((0)) FOR [like]
GO
ALTER TABLE [dbo].[post] ADD  DEFAULT ('ACTIVE') FOR [status]
GO
ALTER TABLE [dbo].[post] ADD  DEFAULT (NULL) FOR [update_by]
GO
ALTER TABLE [dbo].[post_report] ADD  DEFAULT ('UNPROCESS') FOR [report_action]
GO
ALTER TABLE [dbo].[post_view] ADD  DEFAULT ((0)) FOR [view_count]
GO
ALTER TABLE [dbo].[post_view] ADD  DEFAULT ((0)) FOR [like_count]
GO
ALTER TABLE [dbo].[question_type] ADD  DEFAULT ('ACTIVE') FOR [status]
GO
ALTER TABLE [dbo].[questionnaire] ADD  DEFAULT ('ACTIVE') FOR [status]
GO
ALTER TABLE [dbo].[report] ADD  DEFAULT ('PENDING') FOR [status]
GO
ALTER TABLE [dbo].[staff] ADD  DEFAULT ('ACTIVE') FOR [status]
GO
ALTER TABLE [dbo].[student_report] ADD  DEFAULT ((0)) FOR [create_by]
GO
ALTER TABLE [dbo].[student_report] ADD  DEFAULT (NULL) FOR [update_by]
GO
ALTER TABLE [dbo].[student_report] ADD  DEFAULT ('ACTIVE') FOR [status]
GO
ALTER TABLE [dbo].[subject] ADD  DEFAULT ((0)) FOR [create_by]
GO
ALTER TABLE [dbo].[subject] ADD  DEFAULT (NULL) FOR [update_by]
GO
ALTER TABLE [dbo].[subject] ADD  DEFAULT ('ACTIVE') FOR [status]
GO
ALTER TABLE [dbo].[subject_group] ADD  DEFAULT ((0)) FOR [create_by]
GO
ALTER TABLE [dbo].[subject_group] ADD  DEFAULT (NULL) FOR [update_by]
GO
ALTER TABLE [dbo].[subject_group] ADD  DEFAULT ('ACTIVE') FOR [status]
GO
ALTER TABLE [dbo].[subject_group_subject] ADD  DEFAULT ((0)) FOR [create_by]
GO
ALTER TABLE [dbo].[subject_group_subject] ADD  DEFAULT (NULL) FOR [update_by]
GO
ALTER TABLE [dbo].[subject_group_subject] ADD  DEFAULT ('ACTIVE') FOR [status]
GO
ALTER TABLE [dbo].[university] ADD  DEFAULT ('PENDING') FOR [status]
GO
ALTER TABLE [dbo].[university_campus] ADD  DEFAULT ((0)) FOR [create_by]
GO
ALTER TABLE [dbo].[university_campus] ADD  DEFAULT (NULL) FOR [update_by]
GO
ALTER TABLE [dbo].[university_package] ADD  DEFAULT ((0)) FOR [start_view]
GO
ALTER TABLE [dbo].[university_package] ADD  DEFAULT ((0)) FOR [end_view]
GO
ALTER TABLE [dbo].[university_transaction] ADD  DEFAULT ('PENDING') FOR [status]
GO
ALTER TABLE [dbo].[user] ADD  DEFAULT ('ACTIVE') FOR [status]
GO
ALTER TABLE [dbo].[user] ADD  DEFAULT ((0)) FOR [create_by]
GO
ALTER TABLE [dbo].[user] ADD  DEFAULT (NULL) FOR [update_by]
GO
ALTER TABLE [dbo].[user_token] ADD  DEFAULT ((0)) FOR [expired]
GO
ALTER TABLE [dbo].[user_token] ADD  DEFAULT ((0)) FOR [revoked]
GO
ALTER TABLE [dbo].[admin_info]  WITH NOCHECK ADD  CONSTRAINT [FK_user_admin_info] FOREIGN KEY([admin_id])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[admin_info] CHECK CONSTRAINT [FK_user_admin_info]
GO
ALTER TABLE [dbo].[admin_token]  WITH CHECK ADD  CONSTRAINT [FK8lbqdqryjlx0myqdea8lv7nrd] FOREIGN KEY([admin_id])
REFERENCES [dbo].[admin] ([id])
GO
ALTER TABLE [dbo].[admin_token] CHECK CONSTRAINT [FK8lbqdqryjlx0myqdea8lv7nrd]
GO
ALTER TABLE [dbo].[admission]  WITH NOCHECK ADD  CONSTRAINT [FK_admission_university] FOREIGN KEY([university_id])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[admission] CHECK CONSTRAINT [FK_admission_university]
GO
ALTER TABLE [dbo].[admission]  WITH NOCHECK ADD  CONSTRAINT [FK_admission_user] FOREIGN KEY([create_by_staff_id])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[admission] CHECK CONSTRAINT [FK_admission_user]
GO
ALTER TABLE [dbo].[admission_method]  WITH NOCHECK ADD  CONSTRAINT [FK_admission_method_admission] FOREIGN KEY([admission_id])
REFERENCES [dbo].[admission] ([id])
GO
ALTER TABLE [dbo].[admission_method] CHECK CONSTRAINT [FK_admission_method_admission]
GO
ALTER TABLE [dbo].[admission_method]  WITH NOCHECK ADD  CONSTRAINT [FK_admission_method_method] FOREIGN KEY([method_id])
REFERENCES [dbo].[method] ([id])
GO
ALTER TABLE [dbo].[admission_method] CHECK CONSTRAINT [FK_admission_method_method]
GO
ALTER TABLE [dbo].[admission_training_program]  WITH NOCHECK ADD  CONSTRAINT [FK_admission_training_program_admission] FOREIGN KEY([admission_id])
REFERENCES [dbo].[admission] ([id])
GO
ALTER TABLE [dbo].[admission_training_program] CHECK CONSTRAINT [FK_admission_training_program_admission]
GO
ALTER TABLE [dbo].[admission_training_program]  WITH NOCHECK ADD  CONSTRAINT [FK_admission_training_program_major] FOREIGN KEY([major_id])
REFERENCES [dbo].[major] ([id])
GO
ALTER TABLE [dbo].[admission_training_program] CHECK CONSTRAINT [FK_admission_training_program_major]
GO
ALTER TABLE [dbo].[admission_training_program]  WITH NOCHECK ADD  CONSTRAINT [FK_admission_training_program_subject] FOREIGN KEY([main_subject_id])
REFERENCES [dbo].[subject] ([id])
GO
ALTER TABLE [dbo].[admission_training_program] CHECK CONSTRAINT [FK_admission_training_program_subject]
GO
ALTER TABLE [dbo].[admission_training_program_method]  WITH NOCHECK ADD  CONSTRAINT [FK_ad_mt_ad_tr_pr_mt] FOREIGN KEY([admission_method_id])
REFERENCES [dbo].[admission_method] ([id])
GO
ALTER TABLE [dbo].[admission_training_program_method] CHECK CONSTRAINT [FK_ad_mt_ad_tr_pr_mt]
GO
ALTER TABLE [dbo].[admission_training_program_method]  WITH NOCHECK ADD  CONSTRAINT [FK_admission_method_admission_training_program_method] FOREIGN KEY([admission_training_program_id])
REFERENCES [dbo].[admission_training_program] ([id])
GO
ALTER TABLE [dbo].[admission_training_program_method] CHECK CONSTRAINT [FK_admission_method_admission_training_program_method]
GO
ALTER TABLE [dbo].[admission_training_program_subject_group]  WITH NOCHECK ADD  CONSTRAINT [FK_ad_tr_pr_ad_tr_pr_sj_gr] FOREIGN KEY([admission_training_program_id])
REFERENCES [dbo].[admission_training_program] ([id])
GO
ALTER TABLE [dbo].[admission_training_program_subject_group] CHECK CONSTRAINT [FK_ad_tr_pr_ad_tr_pr_sj_gr]
GO
ALTER TABLE [dbo].[admission_training_program_subject_group]  WITH NOCHECK ADD  CONSTRAINT [FK_sb_gr_ad_tr_pr_sb_gr] FOREIGN KEY([subject_group_id])
REFERENCES [dbo].[subject_group] ([id])
GO
ALTER TABLE [dbo].[admission_training_program_subject_group] CHECK CONSTRAINT [FK_sb_gr_ad_tr_pr_sb_gr]
GO
ALTER TABLE [dbo].[ads_package]  WITH NOCHECK ADD  CONSTRAINT [FK_ads_package_user] FOREIGN KEY([create_by])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[ads_package] CHECK CONSTRAINT [FK_ads_package_user]
GO
ALTER TABLE [dbo].[comment]  WITH NOCHECK ADD  CONSTRAINT [FK_comment_comment] FOREIGN KEY([comment_parent_id])
REFERENCES [dbo].[comment] ([id])
GO
ALTER TABLE [dbo].[comment] CHECK CONSTRAINT [FK_comment_comment]
GO
ALTER TABLE [dbo].[comment]  WITH NOCHECK ADD  CONSTRAINT [FK_comment_post] FOREIGN KEY([post_id])
REFERENCES [dbo].[post] ([id])
GO
ALTER TABLE [dbo].[comment] CHECK CONSTRAINT [FK_comment_post]
GO
ALTER TABLE [dbo].[comment]  WITH NOCHECK ADD  CONSTRAINT [FK_comment_user] FOREIGN KEY([commenter_id])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[comment] CHECK CONSTRAINT [FK_comment_user]
GO
ALTER TABLE [dbo].[comment_report_detail]  WITH NOCHECK ADD  CONSTRAINT [FK_comment_report_detail_comment] FOREIGN KEY([comment_id])
REFERENCES [dbo].[comment] ([id])
GO
ALTER TABLE [dbo].[comment_report_detail] CHECK CONSTRAINT [FK_comment_report_detail_comment]
GO
ALTER TABLE [dbo].[comment_report_detail]  WITH NOCHECK ADD  CONSTRAINT [FK_comment_report_detail_report] FOREIGN KEY([report_id])
REFERENCES [dbo].[report] ([id])
GO
ALTER TABLE [dbo].[comment_report_detail] CHECK CONSTRAINT [FK_comment_report_detail_report]
GO
ALTER TABLE [dbo].[consultant_info]  WITH NOCHECK ADD  CONSTRAINT [FK_consultant_info_district] FOREIGN KEY([district_id])
REFERENCES [dbo].[district] ([id])
GO
ALTER TABLE [dbo].[consultant_info] CHECK CONSTRAINT [FK_consultant_info_district]
GO
ALTER TABLE [dbo].[consultant_info]  WITH NOCHECK ADD  CONSTRAINT [FK_consultant_info_province] FOREIGN KEY([province_id])
REFERENCES [dbo].[province] ([id])
GO
ALTER TABLE [dbo].[consultant_info] CHECK CONSTRAINT [FK_consultant_info_province]
GO
ALTER TABLE [dbo].[consultant_info]  WITH NOCHECK ADD  CONSTRAINT [FK_consultant_info_user] FOREIGN KEY([university_id])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[consultant_info] CHECK CONSTRAINT [FK_consultant_info_user]
GO
ALTER TABLE [dbo].[consultant_info]  WITH NOCHECK ADD  CONSTRAINT [FK_consultant_info_ward] FOREIGN KEY([ward_id])
REFERENCES [dbo].[ward] ([id])
GO
ALTER TABLE [dbo].[consultant_info] CHECK CONSTRAINT [FK_consultant_info_ward]
GO
ALTER TABLE [dbo].[consultant_info]  WITH NOCHECK ADD  CONSTRAINT [FK_user_consultant_info] FOREIGN KEY([consultant_id])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[consultant_info] CHECK CONSTRAINT [FK_user_consultant_info]
GO
ALTER TABLE [dbo].[consultant_token]  WITH CHECK ADD  CONSTRAINT [FKk2ifvmqoppr7iifw86golknbw] FOREIGN KEY([consultant_id])
REFERENCES [dbo].[consultant] ([id])
GO
ALTER TABLE [dbo].[consultant_token] CHECK CONSTRAINT [FKk2ifvmqoppr7iifw86golknbw]
GO
ALTER TABLE [dbo].[create_university_request]  WITH NOCHECK ADD  CONSTRAINT [FK_create_university_request_user] FOREIGN KEY([create_by])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[create_university_request] CHECK CONSTRAINT [FK_create_university_request_user]
GO
ALTER TABLE [dbo].[create_university_request]  WITH NOCHECK ADD  CONSTRAINT [FK_user_create_university_request] FOREIGN KEY([confirm_by])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[create_university_request] CHECK CONSTRAINT [FK_user_create_university_request]
GO
ALTER TABLE [dbo].[district_ward]  WITH NOCHECK ADD  CONSTRAINT [FK_district_ward_district] FOREIGN KEY([district_id])
REFERENCES [dbo].[district] ([id])
GO
ALTER TABLE [dbo].[district_ward] CHECK CONSTRAINT [FK_district_ward_district]
GO
ALTER TABLE [dbo].[district_ward]  WITH NOCHECK ADD  CONSTRAINT [FK_district_ward_ward] FOREIGN KEY([ward_id])
REFERENCES [dbo].[ward] ([id])
GO
ALTER TABLE [dbo].[district_ward] CHECK CONSTRAINT [FK_district_ward_ward]
GO
ALTER TABLE [dbo].[function_report]  WITH NOCHECK ADD  CONSTRAINT [FK_function_report_report] FOREIGN KEY([report_id])
REFERENCES [dbo].[report] ([id])
GO
ALTER TABLE [dbo].[function_report] CHECK CONSTRAINT [FK_function_report_report]
GO
ALTER TABLE [dbo].[job]  WITH NOCHECK ADD  CONSTRAINT [FK_job_user] FOREIGN KEY([create_by])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[job] CHECK CONSTRAINT [FK_job_user]
GO
ALTER TABLE [dbo].[post]  WITH NOCHECK ADD  CONSTRAINT [FK_post_user] FOREIGN KEY([create_by])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[post] CHECK CONSTRAINT [FK_post_user]
GO
ALTER TABLE [dbo].[post]  WITH CHECK ADD  CONSTRAINT [FK51aeb5le008k8klgnyfaalmn] FOREIGN KEY([user_id])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[post] CHECK CONSTRAINT [FK51aeb5le008k8klgnyfaalmn]
GO
ALTER TABLE [dbo].[post_report]  WITH NOCHECK ADD  CONSTRAINT [FK_post_report_post] FOREIGN KEY([post_id])
REFERENCES [dbo].[post] ([id])
GO
ALTER TABLE [dbo].[post_report] CHECK CONSTRAINT [FK_post_report_post]
GO
ALTER TABLE [dbo].[post_report]  WITH NOCHECK ADD  CONSTRAINT [FK_post_report_report] FOREIGN KEY([report_id])
REFERENCES [dbo].[report] ([id])
GO
ALTER TABLE [dbo].[post_report] CHECK CONSTRAINT [FK_post_report_report]
GO
ALTER TABLE [dbo].[post_tag]  WITH NOCHECK ADD  CONSTRAINT [FK_post_tag_post] FOREIGN KEY([post_id])
REFERENCES [dbo].[post] ([id])
GO
ALTER TABLE [dbo].[post_tag] CHECK CONSTRAINT [FK_post_tag_post]
GO
ALTER TABLE [dbo].[post_tag]  WITH NOCHECK ADD  CONSTRAINT [FK_post_tag_tag] FOREIGN KEY([tag_id])
REFERENCES [dbo].[tag] ([id])
GO
ALTER TABLE [dbo].[post_tag] CHECK CONSTRAINT [FK_post_tag_tag]
GO
ALTER TABLE [dbo].[post_type]  WITH NOCHECK ADD  CONSTRAINT [FK_post_type_post] FOREIGN KEY([post_id])
REFERENCES [dbo].[post] ([id])
GO
ALTER TABLE [dbo].[post_type] CHECK CONSTRAINT [FK_post_type_post]
GO
ALTER TABLE [dbo].[post_type]  WITH NOCHECK ADD  CONSTRAINT [FK_post_type_type] FOREIGN KEY([type_id])
REFERENCES [dbo].[type] ([id])
GO
ALTER TABLE [dbo].[post_type] CHECK CONSTRAINT [FK_post_type_type]
GO
ALTER TABLE [dbo].[post_view]  WITH NOCHECK ADD  CONSTRAINT [FK_post_view_post] FOREIGN KEY([post_id])
REFERENCES [dbo].[post] ([id])
GO
ALTER TABLE [dbo].[post_view] CHECK CONSTRAINT [FK_post_view_post]
GO
ALTER TABLE [dbo].[province_district]  WITH NOCHECK ADD  CONSTRAINT [FK_province_district_district] FOREIGN KEY([district_id])
REFERENCES [dbo].[district] ([id])
GO
ALTER TABLE [dbo].[province_district] CHECK CONSTRAINT [FK_province_district_district]
GO
ALTER TABLE [dbo].[province_district]  WITH NOCHECK ADD  CONSTRAINT [FK_province_district_province] FOREIGN KEY([province_id])
REFERENCES [dbo].[province] ([id])
GO
ALTER TABLE [dbo].[province_district] CHECK CONSTRAINT [FK_province_district_province]
GO
ALTER TABLE [dbo].[question]  WITH NOCHECK ADD  CONSTRAINT [FK_question_user] FOREIGN KEY([create_by])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[question] CHECK CONSTRAINT [FK_question_user]
GO
ALTER TABLE [dbo].[question_job]  WITH NOCHECK ADD  CONSTRAINT [FK_question_job_job] FOREIGN KEY([job_id])
REFERENCES [dbo].[job] ([id])
GO
ALTER TABLE [dbo].[question_job] CHECK CONSTRAINT [FK_question_job_job]
GO
ALTER TABLE [dbo].[question_job]  WITH NOCHECK ADD  CONSTRAINT [FK_question_job_question] FOREIGN KEY([question_id])
REFERENCES [dbo].[question] ([id])
GO
ALTER TABLE [dbo].[question_job] CHECK CONSTRAINT [FK_question_job_question]
GO
ALTER TABLE [dbo].[question_question_type]  WITH NOCHECK ADD  CONSTRAINT [FK_question_question_type_question] FOREIGN KEY([question_id])
REFERENCES [dbo].[question] ([id])
GO
ALTER TABLE [dbo].[question_question_type] CHECK CONSTRAINT [FK_question_question_type_question]
GO
ALTER TABLE [dbo].[question_question_type]  WITH NOCHECK ADD  CONSTRAINT [FK_question_question_type_question_type] FOREIGN KEY([question_type_id])
REFERENCES [dbo].[question_type] ([id])
GO
ALTER TABLE [dbo].[question_question_type] CHECK CONSTRAINT [FK_question_question_type_question_type]
GO
ALTER TABLE [dbo].[question_type]  WITH NOCHECK ADD  CONSTRAINT [FK_question_type_user] FOREIGN KEY([create_by])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[question_type] CHECK CONSTRAINT [FK_question_type_user]
GO
ALTER TABLE [dbo].[questionnaire]  WITH NOCHECK ADD  CONSTRAINT [FK_questionnaire_user] FOREIGN KEY([create_by])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[questionnaire] CHECK CONSTRAINT [FK_questionnaire_user]
GO
ALTER TABLE [dbo].[questionnaire_question]  WITH NOCHECK ADD  CONSTRAINT [FK_questionnaire_question_question] FOREIGN KEY([question_id])
REFERENCES [dbo].[question] ([id])
GO
ALTER TABLE [dbo].[questionnaire_question] CHECK CONSTRAINT [FK_questionnaire_question_question]
GO
ALTER TABLE [dbo].[questionnaire_question]  WITH NOCHECK ADD  CONSTRAINT [FK_questionnaire_question_questionnaire] FOREIGN KEY([questionnaire_id])
REFERENCES [dbo].[questionnaire] ([id])
GO
ALTER TABLE [dbo].[questionnaire_question] CHECK CONSTRAINT [FK_questionnaire_question_questionnaire]
GO
ALTER TABLE [dbo].[report]  WITH NOCHECK ADD  CONSTRAINT [FK_report_user] FOREIGN KEY([create_by])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[report] CHECK CONSTRAINT [FK_report_user]
GO
ALTER TABLE [dbo].[staff_info]  WITH NOCHECK ADD  CONSTRAINT [FK_staff_info_admin_id_user] FOREIGN KEY([admin_id])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[staff_info] CHECK CONSTRAINT [FK_staff_info_admin_id_user]
GO
ALTER TABLE [dbo].[staff_info]  WITH NOCHECK ADD  CONSTRAINT [FK_staff_info_staff_id_user] FOREIGN KEY([staff_id])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[staff_info] CHECK CONSTRAINT [FK_staff_info_staff_id_user]
GO
ALTER TABLE [dbo].[staff_token]  WITH CHECK ADD  CONSTRAINT [FKlvdeo0dy9o0f97xmokeo29rx0] FOREIGN KEY([staff_id])
REFERENCES [dbo].[staff] ([id])
GO
ALTER TABLE [dbo].[staff_token] CHECK CONSTRAINT [FKlvdeo0dy9o0f97xmokeo29rx0]
GO
ALTER TABLE [dbo].[student_report]  WITH NOCHECK ADD  CONSTRAINT [FK_student_report_user] FOREIGN KEY([user_id])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[student_report] CHECK CONSTRAINT [FK_student_report_user]
GO
ALTER TABLE [dbo].[student_report_mark]  WITH NOCHECK ADD  CONSTRAINT [FK_student_report_mark_student_report] FOREIGN KEY([student_report_id])
REFERENCES [dbo].[student_report] ([id])
GO
ALTER TABLE [dbo].[student_report_mark] CHECK CONSTRAINT [FK_student_report_mark_student_report]
GO
ALTER TABLE [dbo].[student_report_mark]  WITH NOCHECK ADD  CONSTRAINT [FK_student_report_mark_subject_grade_semester] FOREIGN KEY([subject_grade_semester_id])
REFERENCES [dbo].[subject_grade_semester] ([id])
GO
ALTER TABLE [dbo].[student_report_mark] CHECK CONSTRAINT [FK_student_report_mark_subject_grade_semester]
GO
ALTER TABLE [dbo].[subject_grade_semester]  WITH NOCHECK ADD  CONSTRAINT [FK_subject_grade_semester_subject] FOREIGN KEY([subject_id])
REFERENCES [dbo].[subject] ([id])
GO
ALTER TABLE [dbo].[subject_grade_semester] CHECK CONSTRAINT [FK_subject_grade_semester_subject]
GO
ALTER TABLE [dbo].[subject_group_subject]  WITH NOCHECK ADD  CONSTRAINT [FK_subject_group_subject_subject] FOREIGN KEY([subject_id])
REFERENCES [dbo].[subject] ([id])
GO
ALTER TABLE [dbo].[subject_group_subject] CHECK CONSTRAINT [FK_subject_group_subject_subject]
GO
ALTER TABLE [dbo].[subject_group_subject]  WITH NOCHECK ADD  CONSTRAINT [FK_subject_group_subject_subject_group] FOREIGN KEY([subject_group_id])
REFERENCES [dbo].[subject_group] ([id])
GO
ALTER TABLE [dbo].[subject_group_subject] CHECK CONSTRAINT [FK_subject_group_subject_subject_group]
GO
ALTER TABLE [dbo].[test_response]  WITH NOCHECK ADD  CONSTRAINT [FK_test_response_questionnaire] FOREIGN KEY([questionnaire_id])
REFERENCES [dbo].[questionnaire] ([id])
GO
ALTER TABLE [dbo].[test_response] CHECK CONSTRAINT [FK_test_response_questionnaire]
GO
ALTER TABLE [dbo].[test_response_answer]  WITH NOCHECK ADD  CONSTRAINT [FK_test_response_answer_question] FOREIGN KEY([question_id])
REFERENCES [dbo].[question] ([id])
GO
ALTER TABLE [dbo].[test_response_answer] CHECK CONSTRAINT [FK_test_response_answer_question]
GO
ALTER TABLE [dbo].[test_response_answer]  WITH NOCHECK ADD  CONSTRAINT [FK_test_response_answer_test_response] FOREIGN KEY([test_response_id])
REFERENCES [dbo].[test_response] ([id])
GO
ALTER TABLE [dbo].[test_response_answer] CHECK CONSTRAINT [FK_test_response_answer_test_response]
GO
ALTER TABLE [dbo].[type]  WITH NOCHECK ADD  CONSTRAINT [FK_type_user] FOREIGN KEY([create_by])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[type] CHECK CONSTRAINT [FK_type_user]
GO
ALTER TABLE [dbo].[university_campus]  WITH NOCHECK ADD  CONSTRAINT [FK_university_campus_district] FOREIGN KEY([district_id])
REFERENCES [dbo].[district] ([id])
GO
ALTER TABLE [dbo].[university_campus] CHECK CONSTRAINT [FK_university_campus_district]
GO
ALTER TABLE [dbo].[university_campus]  WITH NOCHECK ADD  CONSTRAINT [FK_university_campus_province] FOREIGN KEY([province_id])
REFERENCES [dbo].[province] ([id])
GO
ALTER TABLE [dbo].[university_campus] CHECK CONSTRAINT [FK_university_campus_province]
GO
ALTER TABLE [dbo].[university_campus]  WITH NOCHECK ADD  CONSTRAINT [FK_university_campus_university_info] FOREIGN KEY([university_id])
REFERENCES [dbo].[university_info] ([university_id])
GO
ALTER TABLE [dbo].[university_campus] CHECK CONSTRAINT [FK_university_campus_university_info]
GO
ALTER TABLE [dbo].[university_campus]  WITH NOCHECK ADD  CONSTRAINT [FK_university_campus_ward] FOREIGN KEY([ward_id])
REFERENCES [dbo].[ward] ([id])
GO
ALTER TABLE [dbo].[university_campus] CHECK CONSTRAINT [FK_university_campus_ward]
GO
ALTER TABLE [dbo].[university_info]  WITH NOCHECK ADD  CONSTRAINT [FK_university_info_create_university_request] FOREIGN KEY([create_university_request_id])
REFERENCES [dbo].[create_university_request] ([id])
GO
ALTER TABLE [dbo].[university_info] CHECK CONSTRAINT [FK_university_info_create_university_request]
GO
ALTER TABLE [dbo].[university_info]  WITH NOCHECK ADD  CONSTRAINT [FK_university_info_user] FOREIGN KEY([university_id])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[university_info] CHECK CONSTRAINT [FK_university_info_user]
GO
ALTER TABLE [dbo].[university_package]  WITH NOCHECK ADD  CONSTRAINT [FK_university_package_post] FOREIGN KEY([post_id])
REFERENCES [dbo].[post] ([id])
GO
ALTER TABLE [dbo].[university_package] CHECK CONSTRAINT [FK_university_package_post]
GO
ALTER TABLE [dbo].[university_package]  WITH NOCHECK ADD  CONSTRAINT [FK_university_package_university_transaction] FOREIGN KEY([university_transaction_id])
REFERENCES [dbo].[university_transaction] ([id])
GO
ALTER TABLE [dbo].[university_package] CHECK CONSTRAINT [FK_university_package_university_transaction]
GO
ALTER TABLE [dbo].[university_package]  WITH NOCHECK ADD  CONSTRAINT [FK_university_package_user] FOREIGN KEY([university_id])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[university_package] CHECK CONSTRAINT [FK_university_package_user]
GO
ALTER TABLE [dbo].[university_token]  WITH CHECK ADD  CONSTRAINT [FKh21e3uiqr8nsmg67u2m45xsoi] FOREIGN KEY([university_id])
REFERENCES [dbo].[university] ([id])
GO
ALTER TABLE [dbo].[university_token] CHECK CONSTRAINT [FKh21e3uiqr8nsmg67u2m45xsoi]
GO
ALTER TABLE [dbo].[university_transaction]  WITH NOCHECK ADD  CONSTRAINT [FK_university_transaction_ads_package] FOREIGN KEY([ads_package_id])
REFERENCES [dbo].[ads_package] ([id])
GO
ALTER TABLE [dbo].[university_transaction] CHECK CONSTRAINT [FK_university_transaction_ads_package]
GO
ALTER TABLE [dbo].[university_transaction]  WITH NOCHECK ADD  CONSTRAINT [FK_university_transaction_user] FOREIGN KEY([university_id])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[university_transaction] CHECK CONSTRAINT [FK_university_transaction_user]
GO
ALTER TABLE [dbo].[university_view]  WITH NOCHECK ADD  CONSTRAINT [FK_university_view_user] FOREIGN KEY([university_id])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[university_view] CHECK CONSTRAINT [FK_university_view_user]
GO
ALTER TABLE [dbo].[user_info]  WITH NOCHECK ADD  CONSTRAINT [FK_user_user_info] FOREIGN KEY([user_id])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[user_info] CHECK CONSTRAINT [FK_user_user_info]
GO
ALTER TABLE [dbo].[user_info]  WITH CHECK ADD  CONSTRAINT [FK13ytcbtabcx36t881tfvkcgor] FOREIGN KEY([province_id])
REFERENCES [dbo].[province] ([id])
GO
ALTER TABLE [dbo].[user_info] CHECK CONSTRAINT [FK13ytcbtabcx36t881tfvkcgor]
GO
ALTER TABLE [dbo].[user_info]  WITH CHECK ADD  CONSTRAINT [FKhh596girxkmmdlok3oyb6kmce] FOREIGN KEY([district_id])
REFERENCES [dbo].[district] ([id])
GO
ALTER TABLE [dbo].[user_info] CHECK CONSTRAINT [FKhh596girxkmmdlok3oyb6kmce]
GO
ALTER TABLE [dbo].[user_info]  WITH CHECK ADD  CONSTRAINT [FKv2hrgdlgdyxmx0aoh0bf673i] FOREIGN KEY([ward_id])
REFERENCES [dbo].[ward] ([id])
GO
ALTER TABLE [dbo].[user_info] CHECK CONSTRAINT [FKv2hrgdlgdyxmx0aoh0bf673i]
GO
ALTER TABLE [dbo].[user_token]  WITH NOCHECK ADD  CONSTRAINT [FK_user_token_user] FOREIGN KEY([user_id])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[user_token] CHECK CONSTRAINT [FK_user_token_user]
GO
ALTER TABLE [dbo].[admin]  WITH CHECK ADD CHECK  (([role]='UNIVERSITY' OR [role]='STUDENT' OR [role]='HIGHSCHOOL' OR [role]='CONSULTANT' OR [role]='STAFF' OR [role]='ADMIN'))
GO
ALTER TABLE [dbo].[admin_token]  WITH CHECK ADD CHECK  (([token_type]='BEARER'))
GO
ALTER TABLE [dbo].[admission]  WITH NOCHECK ADD CHECK  (([status]='PENDING' OR [status]='INACTIVE' OR [status]='ACTIVE'))
GO
ALTER TABLE [dbo].[ads_package]  WITH NOCHECK ADD CHECK  (([status]='COMPLETE' OR [status]='INACTIVE' OR [status]='ACTIVE'))
GO
ALTER TABLE [dbo].[comment]  WITH NOCHECK ADD CHECK  (([comment_type]='CHILD' OR [comment_type]='ROOT'))
GO
ALTER TABLE [dbo].[comment]  WITH NOCHECK ADD CHECK  (([comment_status]='ACTIVE' OR [comment_status]='DELETE'))
GO
ALTER TABLE [dbo].[comment_report_detail]  WITH NOCHECK ADD CHECK  (([report_action]='NONE' OR [report_action]='DELETE'))
GO
ALTER TABLE [dbo].[consultant]  WITH CHECK ADD CHECK  (([status]='PENDING' OR [status]='INACTIVE' OR [status]='ACTIVE'))
GO
ALTER TABLE [dbo].[consultant]  WITH CHECK ADD CHECK  (([role]='UNIVERSITY' OR [role]='STUDENT' OR [role]='HIGHSCHOOL' OR [role]='CONSULTANT' OR [role]='STAFF' OR [role]='ADMIN'))
GO
ALTER TABLE [dbo].[consultant_info]  WITH NOCHECK ADD CHECK  (([gender]='FEMALE' OR [gender]='MALE'))
GO
ALTER TABLE [dbo].[consultant_token]  WITH CHECK ADD CHECK  (([token_type]='BEARER'))
GO
ALTER TABLE [dbo].[create_university_request]  WITH NOCHECK ADD CHECK  (([status]='REJECTED' OR [status]='PENDING' OR [status]='ACCEPTED'))
GO
ALTER TABLE [dbo].[create_university_request]  WITH NOCHECK ADD CHECK  (([university_type]='MILITARY' OR [university_type]='PRIVATE' OR [university_type]='PUBLIC'))
GO
ALTER TABLE [dbo].[function_report]  WITH NOCHECK ADD CHECK  (([report_action]='NONE' OR [report_action]='DELETE'))
GO
ALTER TABLE [dbo].[job]  WITH NOCHECK ADD CHECK  (([status]='INACTIVE' OR [status]='ACTIVE'))
GO
ALTER TABLE [dbo].[major]  WITH NOCHECK ADD CHECK  (([status]='INACTIVE' OR [status]='ACTIVE'))
GO
ALTER TABLE [dbo].[method]  WITH NOCHECK ADD CHECK  (([status]='INACTIVE' OR [status]='ACTIVE'))
GO
ALTER TABLE [dbo].[post]  WITH NOCHECK ADD CHECK  (([status]='INACTIVE' OR [status]='PRIVATE' OR [status]='ACTIVE' OR [status]='PENDING'))
GO
ALTER TABLE [dbo].[post_report]  WITH NOCHECK ADD CHECK  (([report_action]='NONE' OR [report_action]='DELETE'))
GO
ALTER TABLE [dbo].[post_tag]  WITH NOCHECK ADD CHECK  (([status]='INACTIVE' OR [status]='ACTIVE'))
GO
ALTER TABLE [dbo].[post_type]  WITH NOCHECK ADD CHECK  (([status]='INACTIVE' OR [status]='ACTIVE'))
GO
ALTER TABLE [dbo].[post_view]  WITH NOCHECK ADD CHECK  (([status]='INACTIVE' OR [status]='ACTIVE'))
GO
ALTER TABLE [dbo].[question]  WITH NOCHECK ADD CHECK  (([type]='CONVENTIONAL' OR [type]='ENTERPRISING' OR [type]='SOCIAL' OR [type]='INVESTIGATIVE' OR [type]='REALISTIC'))
GO
ALTER TABLE [dbo].[question_type]  WITH NOCHECK ADD CHECK  (([status]='INACTIVE' OR [status]='ACTIVE'))
GO
ALTER TABLE [dbo].[questionnaire]  WITH NOCHECK ADD CHECK  (([status]='INACTIVE' OR [status]='ACTIVE'))
GO
ALTER TABLE [dbo].[report]  WITH NOCHECK ADD CHECK  (([report_type]='FUNCTION' OR [report_type]='COMMENT' OR [report_type]='POST'))
GO
ALTER TABLE [dbo].[report]  WITH NOCHECK ADD CHECK  (([status]='COMPLETED' OR [status]='PENDING'))
GO
ALTER TABLE [dbo].[staff]  WITH CHECK ADD CHECK  (([role]='UNIVERSITY' OR [role]='STUDENT' OR [role]='HIGHSCHOOL' OR [role]='CONSULTANT' OR [role]='STAFF' OR [role]='ADMIN'))
GO
ALTER TABLE [dbo].[staff_token]  WITH CHECK ADD CHECK  (([token_type]='BEARER'))
GO
ALTER TABLE [dbo].[student_report]  WITH NOCHECK ADD CHECK  (([status]='INACTIVE' OR [status]='ACTIVE'))
GO
ALTER TABLE [dbo].[student_token]  WITH CHECK ADD CHECK  (([token_type]='BEARER'))
GO
ALTER TABLE [dbo].[subject]  WITH NOCHECK ADD CHECK  (([status]='INACTIVE' OR [status]='ACTIVE'))
GO
ALTER TABLE [dbo].[subject_grade_semester]  WITH NOCHECK ADD CHECK  (([semester]='AVERAGE' OR [semester]='SECOND' OR [semester]='FIRST'))
GO
ALTER TABLE [dbo].[subject_group]  WITH NOCHECK ADD CHECK  (([status]='INACTIVE' OR [status]='ACTIVE'))
GO
ALTER TABLE [dbo].[subject_group_subject]  WITH NOCHECK ADD CHECK  (([status]='INACTIVE' OR [status]='ACTIVE'))
GO
ALTER TABLE [dbo].[tag]  WITH NOCHECK ADD CHECK  (([status]='INACTIVE' OR [status]='ACTIVE'))
GO
ALTER TABLE [dbo].[type]  WITH NOCHECK ADD CHECK  (([status]='INACTIVE' OR [status]='ACTIVE'))
GO
ALTER TABLE [dbo].[university]  WITH CHECK ADD CHECK  (([status]='PENDING' OR [status]='INACTIVE' OR [status]='ACTIVE'))
GO
ALTER TABLE [dbo].[university]  WITH CHECK ADD CHECK  (([role]='UNIVERSITY' OR [role]='STUDENT' OR [role]='HIGHSCHOOL' OR [role]='CONSULTANT' OR [role]='STAFF' OR [role]='ADMIN'))
GO
ALTER TABLE [dbo].[university]  WITH CHECK ADD CHECK  (([type]='MILITARY' OR [type]='PRIVATE' OR [type]='PUBLIC'))
GO
ALTER TABLE [dbo].[university_info]  WITH NOCHECK ADD CHECK  (([university_type]='MILITARY' OR [university_type]='PRIVATE' OR [university_type]='PUBLIC'))
GO
ALTER TABLE [dbo].[university_info]  WITH NOCHECK ADD CHECK  (([type]='PRIVATE' OR [type]='PUBLIC'))
GO
ALTER TABLE [dbo].[university_token]  WITH CHECK ADD CHECK  (([token_type]='BEARER'))
GO
ALTER TABLE [dbo].[university_transaction]  WITH NOCHECK ADD CHECK  (([status]='PENDING' OR [status]='FAILED' OR [status]='CANCELED' OR [status]='PAID'))
GO
ALTER TABLE [dbo].[user]  WITH NOCHECK ADD CHECK  (([provider]='SYSTEM' OR [provider]='GOOGLE'))
GO
ALTER TABLE [dbo].[user]  WITH NOCHECK ADD CHECK  (([status]='DELETED' OR [status]='INACTIVE' OR [status]='ACTIVE'))
GO
ALTER TABLE [dbo].[user_info]  WITH NOCHECK ADD  CONSTRAINT [CK__user_info__educa__01892CED] CHECK  (([education_level]='OTHER' OR [education_level]='HIGH' OR [education_level]='SECONDARY'))
GO
ALTER TABLE [dbo].[user_info] CHECK CONSTRAINT [CK__user_info__educa__01892CED]
GO
ALTER TABLE [dbo].[user_info]  WITH NOCHECK ADD  CONSTRAINT [CK__user_info__gende__009508B4] CHECK  (([gender]='FEMALE' OR [gender]='MALE'))
GO
ALTER TABLE [dbo].[user_info] CHECK CONSTRAINT [CK__user_info__gende__009508B4]
GO
USE [master]
GO
ALTER DATABASE [UAP_DEV] SET  READ_WRITE 
GO
