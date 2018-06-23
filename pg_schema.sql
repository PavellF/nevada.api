--DROP SCHEMA IF EXISTS public CASCADE;
--CREATE SCHEMA public;

CREATE TABLE IF NOT EXISTS profiles (
        id SERIAL NOT NULL,
        profile_id INTEGER DEFAULT NULL REFERENCES profiles(id),
        email VARCHAR(128) NOT NULL,
        password VARCHAR(1024) NOT NULL,
        username VARCHAR(64) NOT NULL,
        sign_date TIMESTAMP NOT NULL,
        user_pic INTEGER DEFAULT NULL,
        popularity INTEGER DEFAULT 0,
        rating INTEGER DEFAULT 0,
        about INTEGER DEFAULT NULL,
        suspended_until TIMESTAMP DEFAULT NULL,
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
        profile_id INTEGER DEFAULT NULL REFERENCES profiles(id),
        full_name VARCHAR(128) DEFAULT NULL,
        gender VARCHAR(64) DEFAULT NULL,
        location VARCHAR(128) DEFAULT NULL,
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
        to_profile INTEGER DEFAULT NULL REFERENCES profiles(id),
        PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS profile_preferences (
    id SERIAL NOT NULL PRIMARY KEY,
    profile_id INTEGER DEFAULT NULL REFERENCES profiles(id),
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
        commentable VARCHAR(36) NOT NULL,
        last_change TIMESTAMP DEFAULT NULL,
        on_profile INTEGER DEFAULT NULL REFERENCES profiles(id),
        PRIMARY KEY (id)
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
        stream_post_message INTEGER DEFAULT NULL REFERENCES stream_post(id),
        PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS photos (
        id SERIAL NOT NULL,
        date TIMESTAMP NOT NULL,
        owner_id INTEGER NOT NULL,
        message VARCHAR(512) DEFAULT NULL,
        small BYTEA NOT NULL,
        medium BYTEA NOT NULL,
        original BYTEA NOT NULL,
        filename VARCHAR(64) NOT NULL,
        visibility VARCHAR(36) NOT NULL,
        PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS _likes (
        id SERIAL NOT NULL,
        date TIMESTAMP NOT NULL,
        rating SMALLINT DEFAULT 0,
        by_user INTEGER NOT NULL,
        liked_message INTEGER DEFAULT NULL REFERENCES messages(id),
        liked_stream_post INTEGER DEFAULT NULL REFERENCES stream_post(id),
        PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS attachment (
        id SERIAL NOT NULL PRIMARY KEY,
        to_message INTEGER DEFAULT NULL REFERENCES messages(id),
        to_stream_post INTEGER DEFAULT NULL REFERENCES stream_post(id),
        photo INTEGER DEFAULT NULL REFERENCES photos(id),
        tag VARCHAR(64) DEFAULT NULL REFERENCES tags (name)
);

ALTER TABLE IF EXISTS stream_post_has_tag
       ADD CONSTRAINT uniq_stream_post_has_tag UNIQUE (stream_post_id, tag);

ALTER TABLE IF EXISTS tokens
       ADD CONSTRAINT uniq_tokens_token UNIQUE (token);

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

ALTER TABLE IF EXISTS profiles
       ADD CONSTRAINT fk_profiles_about
       FOREIGN KEY (about)
       REFERENCES messages;

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
