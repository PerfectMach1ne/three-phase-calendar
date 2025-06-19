WITH cspace AS (
  SELECT u.id,
    cs.user_id AS userid,
    cs.id AS calspace_id,
    cs.tasksevents_id_arr AS taskid_arr,
    cs.timeblockevents_id_arr AS timeblockid_arr,
    cs.textevents_id_arr AS textid_arr
  FROM users u RIGHT JOIN calendarspace cs
  ON u.id = cs.user_id WHERE u.id = 14
)
SELECT json_build_object(
  'userdata', (SELECT json_agg(u) FROM (
    SELECT userid, calspace_id FROM cspace
  ) u),
  'tasks', (SELECT json_agg(t) FROM (
    SELECT id, hashcode, datetime, name, description, viewtype, color, isdone
    FROM taskevents 
    WHERE hashcode IN (
      SELECT UNNEST(taskid_arr) FROM cspace
    )
  ) t),
  'timeblocks', (SELECT json_agg(tb) FROM (
    SELECT id, hashcode, start_datetime, end_datetime, name, description, viewtype, color 
    FROM timeblockevents 
    WHERE hashcode IN (
      SELECT UNNEST(timeblockid_arr) FROM cspace
    )
  ) tb)
) AS calendar_data;

{
  "userdata": [
    {
      "userid": 14,
      "calspace_id": 2
    }
  ],
  "tasks": [
    {
      "id": 1,
      "hashcode": 481499137,
      "datetime": "2025-02-28T13:41:09",
      "name": "Example task",
      "description": "hellooo from postman; todo: build cool stuff",
      "viewtype": "historic_task",
      "color": "#573849",
      "isdone": false
    },
    {
      "id": 5,
      "hashcode": 481499140,
      "datetime": "2025-05-01T13:41:09",
      "name": "Example task",
      "description": "hellooo from postman; todo: build cool stuff",
      "viewtype": "historic_task",
      "color": "#573849",
      "isdone": false
    }
  ],
  "timeblocks": [
    {
      "id": 14,
      "hashcode": -481496967,
      "start_datetime": "2025-05-07T09:35:00",
      "end_datetime": "2025-05-07T15:45:00",
      "name": "Special test task",
      "description": "I like COBOL and I want to learn it. tomorrow I must ASAP go to uncle's home and pester via phone the post office that is supposed to send a courier to me. Don't ya forget the matter of government job office mail, adding stuff to thesis and helping out grandma, also.",
      "viewtype": "static_task",
      "color": "#ffffff"
    },
    {
      "id": 23,
      "hashcode": 481496966,
      "start_datetime": "2025-05-09T08:35:00",
      "end_datetime": "2025-05-09T14:45:00",
      "name": "Special test task",
      "description": "I like COBOL and I want to learn it. tomorrow I must ASAP go to uncle's home and pester via phone the post office that is supposed to send a courier to me. Don't ya forget the matter of government job office mail, adding stuffto thesis and helping out grandma, also.",
      "viewtype": "static_task",
      "color": "ffffff"
    }
  ]
}