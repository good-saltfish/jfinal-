#sql("findCourses")
select c.*, t.name as teacher_name
from course c
         left join teacher t on c.teacher_id = t.id
where c.id in (select course_id from course_selection where student_id = ?)
    #end