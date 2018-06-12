--DROP SCHEMA IF EXISTS public CASCADE;
--CREATE SCHEMA public;

DROP TYPE IF EXISTS ACCESS;
CREATE TYPE ACCESS AS ENUM ('NONE', 'READ', 'READ_WRITE');

DROP TYPE IF EXISTS VISIBILITY;
CREATE TYPE VISIBILITY AS ENUM ('ALL', 'FRIENDS', 'ME');

CREATE TABLE IF NOT EXISTS photos (
        id SERIAL NOT NULL,
        date TIMESTAMP NOT NULL,
        owner_id INTEGER NOT NULL,
        message VARCHAR(512) DEFAULT NULL,
        small BYTEA NOT NULL,
        medium BYTEA NOT NULL,
        original BYTEA NOT NULL,
        PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS _likes (
        id SERIAL NOT NULL,
        date TIMESTAMP NOT NULL,
        RATING SMALLINT DEFAULT 0,
        BY_USER INTEGER NOT NULL,
        PRIMARY KEY(id)
    );

CREATE TABLE IF NOT EXISTS liked_messages (
        liked_message_id INTEGER NOT NULL,
        like_id INTEGER NOT NULL,
        PRIMARY KEY (liked_message_id, like_id)
    );

CREATE TABLE IF NOT EXISTS message_has_photo (
        message_id INTEGER NOT NULL,
        photo_id INTEGER NOT NULL,
        PRIMARY KEY (message_id, photo_id)
    );

CREATE TABLE IF NOT EXISTS messages (
        id SERIAL NOT NULL,
        archived BOOLEAN DEFAULT FALSE,
        content VARCHAR(16384) NOT NULL,
        date TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
        last_change TIMESTAMP DEFAULT NULL,
        rating INTEGER DEFAULT 0,
        author INTEGER NOT NULL,
        reply_to INTEGER DEFAULT NULL,
        priority SMALLINT DEFAULT 0,
        PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS followers (
        id SERIAL NOT NULL,
        follower_id INTEGER NOT NULL,
        followed_id INTEGER NOT NULL,
        since TIMESTAMP DEFAULT NULL,
        mutual BOOLEAN DEFAULT FALSE,
        PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tokens (
     id SERIAL NOT NULL,
     issued_by INTEGER NOT NULL,
     belongs_to_profile INTEGER NOT NULL,
     token VARCHAR(1024) NOT NULL,
     valid_until TIMESTAMP NOT NULL,
     photo_access VARCHAR(36) NOT NULL,
     messages_access VARCHAR(36) NOT NULL,
     friends_access VARCHAR(36) NOT NULL,
     account_access VARCHAR(36) NOT NULL,
     notifications_access VARCHAR(36) NOT NULL,
     stream_access VARCHAR(36) NOT NULL,
     application_access VARCHAR(36) NOT NULL,
     person_info_access VARCHAR(36) NOT NULL,
     is_super_token BOOLEAN DEFAULT FALSE,
     PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS people (
        id SERIAL NOT NULL,
        full_name VARCHAR(128) DEFAULT NULL,
        gender VARCHAR(64) DEFAULT NULL,
        location VARCHAR(128) DEFAULT NULL,
        PRIMARY KEY (ID)
    );

CREATE TABLE IF NOT EXISTS profiles (
        id SERIAL NOT NULL,
        email VARCHAR(128) NOT NULL,
        password VARCHAR(1024) NOT NULL,
        username VARCHAR(64) NOT NULL,
        sign_date TIMESTAMP NOT NULL,
        user_pic INTEGER DEFAULT NULL,
        popularity INTEGER DEFAULT 0,
        rating INTEGER DEFAULT 0,
        about INTEGER DEFAULT NULL,
        person_id INTEGER DEFAULT NULL,
        suspended_until TIMESTAMP DEFAULT NULL,
        PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS applications (
        id SERIAL NOT NULL,
        title VARCHAR(128) NOT NULL,
        suspended_until TIMESTAMP DEFAULT NULL,
        since TIMESTAMP NOT NULL,
        access_key VARCHAR(1024) NOT NULL,
        belongs_to_profile INTEGER NOT NULL,
        PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS tags (
        name VARCHAR(64) NOT NULL,
        PRIMARY KEY(name)
);

CREATE TABLE IF NOT EXISTS guests (
        id SERIAL NOT NULL,
        who INTEGER NOT NULL,
        hidden BOOLEAN NOT NULL,
        when TIMESTAMP NOT NULL,
        PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS profile_preferences (
    profile_id SERIAL NOT NULL PRIMARY KEY REFERENCES profiles(id),
    can_post_on_my_stream VARCHAR(36) NOT NULL DEFAULT 'ME',
    premoderate_followers BOOLEAN NOT NULL DEFAULT 'FALSE'
);

CREATE TABLE IF NOT EXISTS stream_post (
        id SERIAL NOT NULL,
        author INTEGER NOT NULL,
        date TIMESTAMP NOT NULL,
        content VARCHAR(16384) NOT NULL,
        rating INTEGER DEFAULT 0,
        popularity INTEGER DEFAULT 0,
        priority SMALLINT DEFAULT 0,
        visibility VARCHAR(36) NOT NULL,
        last_change TIMESTAMP DEFAULT NULL,
        PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS stream_post_has_photo (
        stream_post_id INTEGER NOT NULL REFERENCES stream_post (id),
        photo_id INTEGER NOT NULL REFERENCES photos (id)
);

CREATE TABLE IF NOT EXISTS profile_has_stream_post (
        profile_id INTEGER NOT NULL REFERENCES profiles (id),
        stream_post_id INTEGER NOT NULL REFERENCES stream_post (id),
);

CREATE TABLE IF NOT EXISTS stream_post_has_tag (
        stream_post_id INTEGER NOT NULL REFERENCES stream_post (id),
        tag VARCHAR NOT NULL REFERENCES tags (name)
);

CREATE TABLE IF NOT EXISTS like_stream_post (
        stream_post_id INTEGER NOT NULL REFERENCES stream_post (id),
        like_id INTEGER NOT NULL REFERENCES _likes (id)
);

CREATE TABLE IF NOT EXISTS guest_to_profile (
        target_profile_id INTEGER NOT NULL REFERENCES profiles (id),
        guest_id INTEGER NOT NULL REFERENCES guests (id)
);

ALTER TABLE IF EXISTS tokens
       ADD CONSTRAINT uniq_tokens_token UNIQUE (token);

ALTER TABLE IF EXISTS liked_messages
       ADD CONSTRAINT uniq_liked_messages_like_id UNIQUE (like_id);

ALTER TABLE IF EXISTS profiles
       ADD CONSTRAINT uniq_profiles_email UNIQUE (email);

ALTER TABLE IF EXISTS profiles
       ADD CONSTRAINT uniq_profiles_username UNIQUE (username);

ALTER TABLE IF EXISTS applications
       ADD CONSTRAINT uniq_applications_title UNIQUE (title);

ALTER TABLE IF EXISTS applications
       ADD CONSTRAINT uniq_applications_access_key UNIQUE (access_key);

ALTER TABLE IF EXISTS followers
       ADD CONSTRAINT uniq_followers UNIQUE (follower_id, followed_id);

ALTER TABLE IF EXISTS tags
       ADD CONSTRAINT uniq_tags_name UNIQUE (name);

ALTER TABLE IF EXISTS tokens
       ADD CONSTRAINT fk_tokens_belongs_to_profile
       FOREIGN KEY (belongs_to_profile)
       REFERENCES profiles;

ALTER TABLE IF EXISTS tokens
       ADD CONSTRAINT fk_tokens_issued_by
       FOREIGN KEY (issued_by)
       REFERENCES applications;

ALTER TABLE IF EXISTS photos
       ADD CONSTRAINT fk_photos_owner_id
       FOREIGN KEY (owner_id)
       REFERENCES profiles;

ALTER TABLE IF EXISTS applications
       ADD CONSTRAINT fk_applications_belongs_to_profile
       FOREIGN KEY (belongs_to_profile)
       REFERENCES profiles;

ALTER TABLE IF EXISTS _likes
       ADD CONSTRAINT fk_likes_by_user
       FOREIGN KEY (by_user)
       REFERENCES profiles;

ALTER TABLE IF EXISTS liked_messages
       ADD CONSTRAINT fk_liked_messages_liked_message_id
       FOREIGN KEY (liked_message_id)
       REFERENCES messages;

ALTER TABLE IF EXISTS liked_messages
       ADD CONSTRAINT fk_liked_messages_like_id
       FOREIGN KEY (like_id)
       REFERENCES _likes;

ALTER TABLE IF EXISTS message_has_photo
       ADD CONSTRAINT fk_message_has_photo_message_id
       FOREIGN KEY (message_id)
       REFERENCES messages;

ALTER TABLE IF EXISTS message_has_photo
       ADD CONSTRAINT fk_message_has_photo_photo_id
       FOREIGN KEY (photo_id)
       REFERENCES photos;

ALTER TABLE IF EXISTS message_has_tag
       ADD CONSTRAINT fk_message_has_tag_message_id
       FOREIGN KEY (message_id)
       REFERENCES messages;

ALTER TABLE IF EXISTS message_has_tag
       ADD CONSTRAINT fk_message_has_tag_tag_name
       FOREIGN KEY (tag_name)
       REFERENCES tags;

ALTER TABLE IF EXISTS profiles
       ADD CONSTRAINT fk_profiles_about
       FOREIGN KEY (about)
       REFERENCES messages;

ALTER TABLE IF EXISTS profiles
       ADD CONSTRAINT fk_profiles_person_id
       FOREIGN KEY (person_id)
       REFERENCES people;

ALTER TABLE IF EXISTS messages
       ADD CONSTRAINT fk_messages_author
       FOREIGN KEY (author)
       REFERENCES profiles;

ALTER TABLE IF EXISTS messages
       ADD CONSTRAINT fk_messages_reply_to
       FOREIGN KEY (reply_to)
       REFERENCES messages;

ALTER TABLE IF EXISTS followers
       ADD CONSTRAINT fk_followers_follower_id
       FOREIGN KEY (follower_id)
       REFERENCES profiles;

ALTER TABLE IF EXISTS followers
       ADD CONSTRAINT fk_followers_followed_id
       FOREIGN KEY (followed_id)
       REFERENCES profiles;

ALTER TABLE IF EXISTS guests
       ADD CONSTRAINT fk_guests_who
       FOREIGN KEY (who)
       REFERENCES profiles;

ALTER TABLE IF EXISTS stream_post
       ADD CONSTRAINT fk_stream_post_author
       FOREIGN KEY (author)
       REFERENCES profiles;
       
INSERT INTO profiles (id, email, password, username, sign_date, user_pic, 
popularity, rating, about, person_id, suspended_until) VALUES (
DEFAULT,'root@test.org','test','root',NOW(),DEFAULT,0,0,DEFAULT,DEFAULT
, DEFAULT); 
INSERT INTO applications (id,title,suspended_until,since,access_key,
belongs_to_profile) VALUES (DEFAULT,'root',DEFAULT,NOW(),'adhi43kmhf',1);
       
INSERT INTO tokens (id,issued_by,belongs_to_profile,token,valid_until,
photo_access,messages_access,friends_access,account_access,
notifications_access,stream_access, application_access, person_info_access,is_super_token) VALUES (
DEFAULT,1,1,'fosof94nswf9wa', NOW()+999999,'READ_WRITE','READ_WRITE',
'READ_WRITE','READ_WRITE','READ_WRITE','READ_WRITE','READ_WRITE','READ_WRITE',TRUE);
       
       
       
       
