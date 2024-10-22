-- 创建表
drop table if exists stud;
create table stud (name string, area string, course string, score int);


-- 插入数据
insert into table stud values('zhang3','bj','math',88);
insert into table stud values('li4','bj','math',99);
insert into table stud values('wang5','sh','chinese',92);
insert into table stud values('zhao6','sh','chinese',54);
insert into table stud values('tian7','bj','chinese',91);

select * from stud;


-- 2.4：把同一分组的不同行的数据聚合成一个集合
select course, collect_set(area), avg(score) from stud group by course;
chinese ["sh","bj"]     79.0
math    ["bj"]  93.5

-- 用下标可以取某一个
select course, collect_set(area)[0], avg(score) from stud group by course;
chinese sh      79.0
math    bj      93.5

-- collect_set(area)中值用’|'分割

select course, concat_ws('|',collect_set(area)), avg(score) from stud group by course;
chinese     sh|bj     79.0
math         bj       93.5


-- 补充：collect_list: 与collect_set的区别就是列的值不去重；
select course, concat_ws('|',collect_list(area)), avg(score) from stud group by course;
chinese     sh|sh|bj     79.0
math         bj|bj       93.5
