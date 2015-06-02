-- phpMyAdmin SQL Dump
-- version 3.5.8.1deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 20, 2013 at 02:58 PM
-- Server version: 5.5.31-0ubuntu0.13.04.1
-- PHP Version: 5.4.9-4ubuntu2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `qa`
--
CREATE DATABASE `qa` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `qa`;

-- --------------------------------------------------------

--
-- Table structure for table `baiduuser`
--

CREATE TABLE IF NOT EXISTS `baiduuser` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(30) NOT NULL COMMENT '用户名',
  `gradeIndex` int(30) NOT NULL COMMENT '等级',
  `grAnswerNum` int(30) DEFAULT NULL COMMENT ' 采纳率',
  `carefield` varchar(30) NOT NULL COMMENT '擅长领域',
  `goodRate` int(30) NOT NULL COMMENT '采纳率',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userName` (`userName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `qapair`
--

CREATE TABLE IF NOT EXISTS `qapair` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `qid` varchar(30) NOT NULL COMMENT '查询id',
  `question` varchar(100) NOT NULL COMMENT '问题',
  `description` varchar(300) NOT NULL DEFAULT '' COMMENT '问题描述摘要',
  `category` varchar(30) NOT NULL COMMENT '分类',
  `q_time` varchar(30) NOT NULL COMMENT '问题提问时间',
  `isAdopted` varchar(10) NOT NULL DEFAULT '' COMMENT '百度中是否已经采纳答案的标志 ',
  `asker_id` varchar(30) NOT NULL COMMENT '回答者的id',
  `answer` text NOT NULL COMMENT '"答案内容 采纳的答案或者第一条答案" ',
  `comment` varchar(100) NOT NULL DEFAULT '' COMMENT '评论',
  `asker_comment` varchar(100) NOT NULL DEFAULT '' COMMENT '回答者的评论',
  `answer_time` varchar(30) NOT NULL COMMENT '答案的回答时间 ',
  `isBest` int(10) NOT NULL DEFAULT '0' COMMENT '是否被采纳的标记 1 表示被采纳 0 表示未被采纳',
  `answer_id` int(11) NOT NULL COMMENT '回答者的id，参考baiduuser中的id字段',
  `querytime` varchar(30) NOT NULL COMMENT '查询时间，即网页内容爬取时间 ',
  PRIMARY KEY (`id`),
  KEY `answer_id` (`answer_id`),
  KEY `qid` (`qid`),
  KEY `qid_2` (`qid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `qapair_resultlist`
--

CREATE TABLE IF NOT EXISTS `qapair_resultlist` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `qid` varchar(30) NOT NULL COMMENT '百度中该问答对的id ',
  `rankid` int(10) NOT NULL COMMENT '该问答对在查询结果中的排序顺序id ',
  `title` varchar(50) NOT NULL COMMENT '问句',
  `answer` text NOT NULL COMMENT '答案摘要 ',
  `answer_time` varchar(50) NOT NULL COMMENT '答案的回答时间 ',
  `link` varchar(100) NOT NULL COMMENT 'url',
  `querytime` varchar(50) CHARACTER SET latin1 NOT NULL COMMENT '查询时间，即网页内容爬取时间 ',
  `queryid` varchar(100) DEFAULT NULL COMMENT ' query表中的问句的id',
  `finished` int(10) DEFAULT '0' COMMENT '该i列中的link是否已经下载过的标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `qid` (`qid`),
  KEY `queryid` (`queryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `query`
--

CREATE TABLE IF NOT EXISTS `query` (
  `id` int(100) NOT NULL AUTO_INCREMENT,
  `queryid` varchar(100) NOT NULL,
  `query` text NOT NULL,
  `finished` int(10) DEFAULT '0' COMMENT '是否已经下载过的标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `queryid` (`queryid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `query`
--

INSERT INTO `query` (`id`, `queryid`, `query`, `finished`) VALUES
(1, '0001', '什么是股票?', 0),
(2, '0002', '股票行情', 0);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `qapair`
--
ALTER TABLE `qapair`
  ADD CONSTRAINT `qapair_ibfk_1` FOREIGN KEY (`answer_id`) REFERENCES `baiduuser` (`id`),
  ADD CONSTRAINT `qapair_ibfk_2` FOREIGN KEY (`qid`) REFERENCES `qapair_resultlist` (`qid`);

--
-- Constraints for table `qapair_resultlist`
--
ALTER TABLE `qapair_resultlist`
  ADD CONSTRAINT `qapair_resultlist_ibfk_1` FOREIGN KEY (`queryid`) REFERENCES `query` (`queryid`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;