#sql("findCourses")
select c.*, t.name as teacher_name
from course c
         left join teacher t on c.teacher_id = t.id
where c.id in (select course_id from course_selection where student_id = #student_id#)
#end

use student;
-- 删除已存在的表（注意顺序：先删除有外键约束的表）
DROP TABLE IF EXISTS course_selection;
DROP TABLE IF EXISTS score;
DROP TABLE IF EXISTS course;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS teacher;

-- 创建教师表
CREATE TABLE teacher (
    id INT PRIMARY KEY AUTO_INCREMENT,
    teacher_no VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL,
    title VARCHAR(50),
    phone VARCHAR(20),
    email VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建学生表
CREATE TABLE student (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_no VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,  -- 添加密码字段
    gender CHAR(1),
    age INT,
    phone VARCHAR(20),
    email VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    avatar VARCHAR(255) COMMENT '头像路径'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建课程表
CREATE TABLE course (
    id INT PRIMARY KEY AUTO_INCREMENT,
    course_code VARCHAR(20) NOT NULL UNIQUE,
    course_name VARCHAR(100) NOT NULL,
    credit DECIMAL(3,1),
    teacher_id INT,
    description TEXT,
    status VARCHAR(20) DEFAULT 'not_started' COMMENT '课程状态：not_started(未开课), in_progress(进行中), ended(已结束)',
    class_time VARCHAR(50) COMMENT '上课时间',
    classroom VARCHAR(50) COMMENT '上课地点',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (teacher_id) REFERENCES teacher(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建成绩表
CREATE TABLE score (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    score DECIMAL(5,2),
    semester VARCHAR(20),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (course_id) REFERENCES course(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建选课表
CREATE TABLE course_selection (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL COMMENT '学生ID',
    course_id INT NOT NULL COMMENT '课程ID',
    select_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (course_id) REFERENCES course(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='选课表';

-- 创建通知表
CREATE TABLE notification (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL COMMENT '通知标题',
    content TEXT NOT NULL COMMENT '通知内容',
    type VARCHAR(20) DEFAULT 'info' COMMENT '通知类型：info(普通), warning(警告), success(成功)',
    is_read BOOLEAN DEFAULT FALSE COMMENT '是否已读',
    student_id INT COMMENT '接收学生ID，为空表示全体学生',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知表';

-- 插入一些测试数据
INSERT INTO teacher (teacher_no, name, title) VALUES 
('T001', '张三', '教授'),
('T002', '李四', '副教授'),
('T003', '王五', '讲师');

INSERT INTO student (student_no, name, password, gender, age) VALUES 
('S001', '学生1', '123456', 'M', 20),
('S002', '学生2', '123456', 'F', 19),
('S003', '学生3', '123456', 'M', 21);

INSERT INTO course (course_code, course_name, credit, teacher_id, status, class_time, classroom) VALUES 
('C001', '高等数学', 4.0, 1, 'not_started', '周一 1-2节', '教学楼101'),
('C002', '大学英语', 3.0, 2, 'not_started', '周二 3-4节', '教学楼102'),
('C003', '计算机基础', 2.0, 3, 'not_started', '周三 5-6节', '教学楼103');
ALTER TABLE `student` ADD COLUMN `role` INT DEFAULT 0;
ALTER TABLE `course` ADD COLUMN `sum_stu` INT DEFAULT 0 COMMENT '总人数';
ALTER TABLE `course` ADD COLUMN `have_stu` INT DEFAULT 0 COMMENT '目前可选人数';