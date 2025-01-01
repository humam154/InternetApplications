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

- `/files`
    - `/upload`
    - `/update/{id}`
    - `/accept/{id}`
    - `/reject/{id}`
    - `/download/{id}`
    - `/downloadmany`
    - [X] `/groupfiles/{gid}` 
    > will be `/groupfiles/{gid}?q=` for filtering between [in_use, accepted, all], result varies depeding on user token (group owner or not)
    - [X] `/pending/{gid}`
    - [ ] `/filechecks/{fid}`
    - [ ] `/groupfilechecks/{gid}`
    - [ ] `/userfilechecks`
    - [ ] `/search?q=`

- [X] `/groups`
    - [X] `/isOwner`
    - [X] `/create`
    - [X] `/delete/{gid}`
    - [ ] `/removemember`
    - [x] `/search?q=`

- `/invitations`
    - [X] `/invite`
    - [X] `/accept/{id}`
    - [X] `/reject/{id}`
    - [ ] `/revoke`
    - [X] `/inbox`
    - [X] `/outbox`
    - [ ] `/search?q=`
    
- [ ] `/logs`
    ...