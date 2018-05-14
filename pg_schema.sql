DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

DROP TYPE IF EXISTS ACCESS;
CREATE TYPE ACCESS AS ENUM ('NONE', 'READ', 'READ_WRITE');

DROP TYPE IF EXISTS VISIBILITY;
CREATE TYPE VISIBILITY AS ENUM ('ALL', 'FRIENDS', 'ME');

CREATE TABLE IF NOT EXISTS photos (
        id SERIAL NOT NULL,
        date TIMESTAMP NOT NULL,
        owner_id INTEGER NOT NULL,
        visibility_level VISIBILITY NOT NULL,
        album_id INTEGER NOT NULL,
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
    
CREATE TABLE IF NOT EXISTS message_has_tag (
        message_id INTEGER NOT NULL,
        tag_name VARCHAR (64) NOT NULL,
        PRIMARY KEY (message_id, tag_name)
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
    
CREATE TABLE IF NOT EXISTS friendship (
        id SERIAL NOT NULL,
        user_id INTEGER NOT NULL,
        friend_id INTEGER NOT NULL,
        since TIMESTAMP DEFAULT NULL,
        mutual BOOLEAN DEFAULT FALSE,
        PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS albums (
        id SERIAL NOT NULL,
        owner_id INTEGER NOT NULL,
        title VARCHAR(64) NOT NULL,
        visibility_level VISIBILITY NOT NULL,
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
     chat_access VARCHAR(36) NOT NULL,
     stream_access VARCHAR(36) NOT NULL,
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
        belongs_to INTEGER NOT NULL,
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
       
ALTER TABLE IF EXISTS friendship 
       ADD CONSTRAINT uniq_friendship UNIQUE (user_id, friend_id);
       
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
       
ALTER TABLE IF EXISTS photos
       ADD CONSTRAINT fk_photos_album_id
       FOREIGN KEY (album_id) 
       REFERENCES albums;    
       
ALTER TABLE IF EXISTS applications 
       ADD CONSTRAINT fk_applications_belongs_to_profile
       FOREIGN KEY (belongs_to_profile) 
       REFERENCES profiles;
       
ALTER TABLE IF EXISTS _likes 
       ADD CONSTRAINT fk_likes_by_user
       FOREIGN KEY (by_user) 
       REFERENCES profiles;
       
ALTER TABLE IF EXISTS profiles 
       ADD CONSTRAINT fk_profiles_belongs_to
       FOREIGN KEY (belongs_to) 
       REFERENCES people;

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

ALTER TABLE IF EXISTS messages 
       ADD CONSTRAINT fk_messages_author 
       FOREIGN KEY (author) 
       REFERENCES profiles;

ALTER TABLE IF EXISTS messages
       ADD CONSTRAINT fk_messages_reply_to
       FOREIGN KEY (reply_to) 
       REFERENCES messages;
       
ALTER TABLE IF EXISTS friendship
       ADD CONSTRAINT fk_friendship_user_id
       FOREIGN KEY (user_id) 
       REFERENCES profiles;
       
ALTER TABLE IF EXISTS friendship
       ADD CONSTRAINT fk_friendship_friend_id
       FOREIGN KEY (friend_id) 
       REFERENCES profiles;
       
ALTER TABLE IF EXISTS albums
       ADD CONSTRAINT fk_albums_owner_id
       FOREIGN KEY (owner_id) 
       REFERENCES profiles;
       
       
       
       
       
       
       

