create table tag_follows (
  user_id varchar(255) not null,
  tag_id varchar(255) not null,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  primary key(user_id, tag_id)
);
