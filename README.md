Source Safe -  File Management System
=========================

## Technologies Used

[![tech stack](https://skillicons.dev/icons?i=spring,java,mysql,react,vite,html,css)](https://skillicons.dev)

## Documentation

### Endpoints

- `/auth`
    - `/register`
    - `/authenticate`
    - [ ] `/confirmemail`
    - [ ] `/forgotpassword`

- `/user`
    - `/change`
    - `/update`
    - [x] `/search?q=`

- `/files`
    - `/upload`
    - `/update/{id}`
    - `/accept/{id}`
    - `/reject/{id}`
    - `/download/{id}`
    - `/downloadmany`
    - [X] `/groupfiles/{gid}?filter=` 
    > `/groupfiles/{gid}?filter=` for filtering between [in_use, accepted, pending], result varies depending on user token (group owner or not)
    - [ ] `/groupfiles/{gid}?filter=in_use`
    - [X] ~~`/pending/{gid}`~~
    - [ ] `/filechecks/{fid}`
    - [ ] `/groupfilechecks/{gid}`
    - [ ] `/userfilechecks`
    - [ ] `/search?q=`

- [X] `/groups`
    - [X] `/isOwner`
    - [X] `/create`
    - [X] `/delete/{gid}`
    - [ ] `/removemember`

- `/invitations`
    - [X] `/invite`
    - [X] `/accept/{id}`
    - [X] `/reject/{id}`
    - [X] `/revoke/{id}`
    - [X] `/inbox`
    - [X] `/outbox`
    - [ ] `/search?q=`
    
- [ ] `/logs`
    ...