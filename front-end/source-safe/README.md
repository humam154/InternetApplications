Source-Safe - Client Side
===

## How to run

First, confirm that you have Node and npm on your system:
```shell
node --version
```
```shell
npm --version
```

Then install the dependencies:
```shell
npm i
```
Now you're ready to run:

```shell
npm run dev
```

You will see the following screen on `http://localhost:5173/` :

![alt text](image.png)

This is the greeting screen of the application, it will be modified later.

### What should be done

- `/user`
    - [ ] `/change`
    - [ ] `/update`

- `/files`
    - [X] `/upload`
    - [X] `/update/{id}`
    - [ ] `/accept/{id}`
    - [ ] `/reject/{id}`
    - [X] `/download/{id}`
    - [ ] `/downloadmany`
    - [X] `/groupfiles/{gid}?filter=` 
    - [X] ~~`/pending/{gid}`~~
    - [ ] `/filechecks/{fid}`
    - [ ] `/groupfilechecks/{gid}`
    - [ ] `/userfilechecks`
    - [ ] `/search?q=`

- [X] `/groups`
    - [X] `/create`
    - [ ] `/delete/{gid}`
    - [ ] `/removemember`
    - [ ] `/search?q=`

- `/invitations`
    - [X] `/invite`
    - [X] `/accept/{id}`
    - [X] `/reject/{id}`
    - [X] `/revoke/{id}`
    - [X] `/inbox`
    - [X] `/outbox`
    - [ ] `/search?q=`