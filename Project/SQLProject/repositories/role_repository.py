from models.role_orm import Role
from base.sql_base import Session


def getRoles():
    session = Session()
    roles = session.query(Role).all()
    return roles
