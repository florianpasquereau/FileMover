File Mover
==========
Small Java library to move file form one place to another.

Requirement:
=========
TO move a file, a json file with the same name must exist.
This json is to store details about the file who need to be moved.
By default, the json file must contain following details:
```
{
    "sha256" : "61444B5F007D42B73735113AA6655AB72F07222E78097E0EB123EBCC115A2359"
}
```

Details:
==========
Java program developed in Java 11.

FileMover `SOURCE FOLDER` `DEST FOLDER`