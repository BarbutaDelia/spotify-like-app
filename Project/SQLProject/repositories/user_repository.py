from typing import Union

from sqlalchemy import update, select

from models.role_orm import Role
from models.user_orm import User
from base.sql_base import Session
from models.users_roles_orm import user_roles_relationship
from datetime import datetime, timedelta
from typing import Union
from fastapi import Depends, FastAPI, HTTPException, status
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm
from jose import JWTError, jwt
import uuid
from passlib.context import CryptContext
from pydantic import BaseModel
from jose import JWTError, jwt, jws
from jose.exceptions import JWSError, JWSSignatureError, ExpiredSignatureError

SECRET_KEY = "843e2b8320dfb81c3222df28d32ee3327247ddfba0e81dbcec57540911098488"
ALGORITHM = "HS256"


def getUsers():
    session = Session()
    users = session.query(User).all()
    return users

def createUser(username, password):
    session = Session()
    user = User(username, password)
    try:
        session.add(user)
        session.commit()
        return "User created successfully!"  # poate asta ar fi bine sa returneze id ul din baza de date
    except Exception as exc:
        return (f"Error! Failed to add user - {exc}")


def changePassword(user_id, newPassword):
    session = Session()
    stmt = (
        update(User).
            where(User.id == user_id).
            values(password=newPassword)
    )
    try:
        session.execute(stmt)
        session.commit()
        return "Password successfully changed!"
    except Exception as exc:
        return (f"Failed to change password - {exc}")


def addRole(username, newRole):
    session = Session()
    roleId = session.query(Role).filter_by(role_name=newRole).first().id
    userId = session.query(User).filter_by(username=username).first().id

    # userRole = user_roles_relationship(user_id = userId, role_id = roleId)
    try:
        # User.insert(roles=roleId)
        # User.roles = Role(roleId)
        # User.roles = [session.query(Role).filter_by(role_name=newRole).first()]
        # session.commit()
        stmt = user_roles_relationship.insert().values(user_id=userId, role_id=roleId)
        session.execute(stmt)
        session.commit()
        return "Role successfully added!"
    except Exception as exc:
        return (f"Error! Failed to change role - {exc}")
    # roleId = role.id
    # print(roleId)
    # user_roles_relationship.insert(role=newRole)


def getInfo(user_id):
    session = Session()
    # password = session.query(User).filter_by(username=username).first().password
    roles = session.query(User).filter_by(id=user_id).first().roles
    username = session.query(User).filter_by(id=user_id).first().username
    rolesList = []
    for i in roles:
        rolesList.append(i.role_name)
    return (username, rolesList)

def getIdFromUsername(username):
    session = Session()
    userId = session.query(User).filter_by(username=username).first().id
    if userId is not None:
        return userId
    else:
        return "Error! Invalid username"

def verifyCredentials(username, password):
    session = Session()
    if session.query(User).filter_by(username=username, password=password).first() is not None:
        return True
    return False


def authorize(token):
    f = open("blacklist.txt", "r+")
    content = f.readlines()
    for line in content:
        if token == line.strip("\n"):
            return "Error! Token has been blacklisted!"
    try:
        jws.verify(token, SECRET_KEY, ALGORITHM)
    except JWSError:
        f.write(token + "\n")
        return "Error! Signature verification failed!"
    try:
        payload = jwt.decode(token, SECRET_KEY, ALGORITHM)
        session = Session()
        roles = session.query(User).filter_by(id=payload["sub"]).first().roles
        rolesList = []
        for role in roles:
            rolesList.append(role.role_name)
        for roleInJws in payload["role"]:
            if roleInJws not in rolesList:
                return "Error! Conflicting roles!"
        return str(payload["sub"] + "-" + str(payload["role"]))
    except ExpiredSignatureError:
        f.write(token + "\n")
        return "Error! Expired Signature!"


def getUserId(username):
    session = Session()
    id = session.query(User).filter_by(username=username).first().id
    return id


def getUserRoles(user_id):
    session = Session()
    roles = session.query(User).filter_by(id=user_id).first().roles
    rolesList = []
    for role in roles:
        rolesList.append(role.role_name)
    return rolesList


def create_access_token(data: dict, expires_delta: Union[timedelta, None] = None):
    to_encode = data.copy()
    if expires_delta:
        expire = datetime.utcnow() + expires_delta
    else:
        expire = datetime.utcnow() + timedelta(minutes=15)
    to_encode.update({"exp": expire})  # expiration date
    to_encode.update({"iss": "http://127.0.0.1:8000"})  # url which generated the token
    to_encode.update({"jti": str(uuid.uuid4())})
    userRoles = getUserRoles(data.get("sub"))
    to_encode.update({"role": userRoles})

    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt

def validateJws(token):
    f = open("blacklist.txt", "r+")
    content = f.readlines()
    for line in content:
        if token in line.strip("\n"):
            return "Error! Token has been blacklisted!"
    try:
        jws.verify(token, SECRET_KEY, ALGORITHM)
    except JWSError:
        f.write(token + "\n")
        return "Error! Signature verification failed!"
    try:
        payload = jwt.decode(token, SECRET_KEY, ALGORITHM)
        return payload["sub"] + ", " + str(payload["role"])
    except ExpiredSignatureError:
        f.write(token + "\n")
        return "Error! Expired Signature!"

def invalidateJws(token):
    f = open("blacklist.txt", "a")
    try:
        jws.verify(token, SECRET_KEY, ALGORITHM)
    except JWSError:
        f.write(token + "\n")
        return "Error! Signature verification failed!"
    try:
        jwt.decode(token, SECRET_KEY, ALGORITHM)
        f.write(token + "\n")
        return "Successful logout!"
    except ExpiredSignatureError:
        f = open("blacklist.txt", "a")
        f.write(token + "\n")
        return "Error! Expired Signature!"
