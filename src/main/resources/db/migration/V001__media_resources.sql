-- MEDIA RESOURCE --
create table if not exists public.media_resources
(
    id                 bigint       not null
        constraint users_pk primary key,
    hash_key           varchar(500) not null,
    name               varchar(255),
    type               varchar(255) not null,
    status             varchar(255),
    privacy_type       varchar(255),
    privacy_owner_guid uuid,
    size               int,
    meta_data          jsonb,
    created_by         uuid,
    created_at         timestamp default now(),
    updated_at         timestamp
);

comment on table public.media_resources is 'Table for all media resources';
comment on column public.media_resources.id is 'Identifier of resource';
comment on column public.media_resources.hash_key is 'Hash unique identifier of resource';

create sequence if not exists media_resources_id_seq;
create index if not exists media_resources_hash_idx ON public.media_resources (hash_key);
