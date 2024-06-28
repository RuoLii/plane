DROP DATABASE IF EXISTS plane;
CREATE DATABASE plane;
use plane;


DROP TABLE IF EXISTS user;
CREATE TABLE user(
    id INT AUTO_INCREMENT primary key comment 'ID',
    name VARCHAR(30) NOT NULL comment '姓名',
    username VARCHAR(20) NOT NULL comment '账号',
    password VARCHAR(20) NOT NULL comment '密码',
    reservation INT DEFAULT 0 comment '订票量',
    role INT DEFAULT 0 comment '权限',
    INDEX idx_username(username)
);

DROP TABLE IF EXISTS plane;
CREATE TABLE plane(
    flightNumber VARCHAR(20) primary key comment '航班号',
    planeType VARCHAR(30) NOT NULL comment '飞机型号',
    flyTime DATE NOT NULL DEFAULT '2004-01-03' comment '飞行日期',
    startStation VARCHAR(50) NOT NULL comment '起始站',
    endStation VARCHAR(50) NOT NULL comment '终点站',
    memberNum INT NOT NULL comment '成员定量',
    remainTickets INT NOT NULL comment '余票量',
    economyMoney INT NOT NULL comment '经济舱金额',
    firstMoney INT NOT NULL comment '头等舱金额',
    INDEX idx_flt(flightNumber)
);

DROP TABLE IF EXISTS ticketing;
CREATE TABLE ticketing(
    id INT PRIMARY KEY AUTO_INCREMENT comment '订票编号',
    username VARCHAR(20) NOT NULL comment '乘客账号',
    flightNumber VARCHAR(20) NOT NULL comment '航班号',
    navigationLevel VARCHAR(20) NOT NULL comment '航位等级',
    state ENUM('已购票', '已进站', '已出站', '已退票') NOT NULL comment '状态',
    CONSTRAINT fk_ticketing_user FOREIGN KEY (username) REFERENCES user(username),
    CONSTRAINT fk_ticketing_plane FOREIGN KEY (flightNumber) REFERENCES plane(flightNumber)
);