create table hibernate_sequence
(
  next_val bigint null
);

create table user
(
  id         bigint       not null
    primary key,
  created    datetime     not null,
  disabled   datetime     null,
  email      varchar(255) null,
  email_hash char(24)     null,
  name       varchar(255) null,
  password   varchar(255) null,
  removed    datetime     null,
  constraint user_email_hash_uindex
  unique (email_hash)
);

create table note
(
  id        bigint       not null
    primary key,
  content   varchar(255) not null,
  created   datetime     not null,
  encrypted bit          not null,
  expires   datetime     null,
  received  datetime     null,
  recipient bigint       not null,
  removed   datetime     null,
  sender    bigint       not null,
  sent      datetime     null,
  subject   varchar(255) not null,
  constraint note_user_id_fk
  foreign key (recipient) references user (id),
  constraint note_user_id_fk_2
  foreign key (sender) references user (id)
);

create trigger hash_email_on_insert
  before INSERT
  on user
  for each row
  BEGIN
    SET NEW.email_hash = TO_BASE64(UNHEX(MD5(NEW.email)));
  END;

create trigger hash_email_on_update
  before UPDATE
  on user
  for each row
  BEGIN
    SET NEW.email_hash = TO_BASE64(UNHEX(MD5(NEW.email)));
  END;


