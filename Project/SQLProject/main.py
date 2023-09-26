# python -m pip install mariadb sqlalchemy
from datetime import timedelta
from wsgiref.simple_server import make_server

from spyne import ServiceBase, rpc, String, Application, Boolean
from spyne.protocol.soap import Soap11
from spyne.server.wsgi import WsgiApplication
import logging
from fastapi import Depends, FastAPI, HTTPException, status
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm
from jose import JWTError, jwt
from passlib.context import CryptContext

from repositories.role_repository import getRoles
from repositories.user_repository import changePassword, addRole, getInfo, verifyCredentials, \
    createUser, getUsers, authorize, create_access_token, getUserId, invalidateJws, validateJws, getIdFromUsername

ACCESS_TOKEN_EXPIRE_MINUTES = 60

class UserManagementService(ServiceBase):
    @rpc(String, String, String, String, _returns=String)
    def createUser(self, username, password, role_name, token):
        response = authorize(token)
        if "Error!" not in response:
            role = response.split("-")[1].strip("[").strip("]").strip("'")
            if role == "administrator aplicatie":
                message1 = createUser(username, password)
                message2 = addRole(username, role_name)
                return message1 + message2
            else:
                return "Error! Forbidden Access"
        else:
            return response

    @rpc(String, _returns=String)
    def getUsers(self, token):
        response = authorize(token)
        if "Error!" not in response:
            role = response.split("-")[1].strip("[").strip("]").strip("'")
            if role == "administrator aplicatie":
                allUsers = ""
                for user in getUsers():
                    allUsers += (f"{user.id} - {user.username} - {user.password} - roles: ") #in niciun caz nu ar trebui returnata parola
                    for role in user.roles:
                        allUsers += (f"{role.role_name} ")
                    allUsers += "\n"
                return allUsers
            else:
                return "Error! Forbidden Access"
        else:
            return response

    @rpc(String, String, String, _returns=String)
    def changePassword(self, user_id, newPassword, token):
        response = authorize(token)
        if "Error!" not in response:
            userIdFromToken = response.split("-")[0]
            if user_id == userIdFromToken:
                return changePassword(int(user_id), newPassword)
            else:
                return "Error! Forbidden Access"
        else:
            return response

    @rpc(String, String, _returns=String)
    def getInfo(self, user_id, token):
        response = authorize(token)
        if "Error!" not in response:
            role = response.split("-")[1].strip("[").strip("]").strip("'")
            if role == "administrator aplicatie":
                username, rolesList = getInfo(user_id)
                userInfo = username + ": "
                userInfo += ', '.join(map(str, rolesList))
                return userInfo
            else:
                return "Error! Forbidden Access"
        else:
            return response

    @rpc(String, _returns=String)
    def getRoles(self, token): #redenumeste metoda in getUserInfo
        response = authorize(token)
        if "Error!" not in response:
            role = response.split("-")[1].strip("[").strip("]").strip("'")
            if role == "administrator aplicatie":
                roles = ""
                for r in getRoles():
                    roles += r.role_name + ", "
                return roles
            else:
                return "Error! Forbidden Access"
        else:
            return response

    @rpc(String, String, _returns=String)
    def verifyCredentials(self, username, password):
        if verifyCredentials(username, password):
            user_id = getUserId(username)
            access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
            access_token = create_access_token(
                data={"sub": str(user_id)}, expires_delta=access_token_expires
            )
            return access_token
        else:
            return "Authentication failed!"

    @rpc(String, _returns=String)
    def authorize(self, token):
        return authorize(token)

    @rpc(String, _returns=String)
    def logout(self, token):
        return invalidateJws(token)
    #TODO aici e putin ciudat ca as putea trimite sa invalidez tokenul altcuiva de exemplu

    # @rpc(String, _returns=String)
    # def validateToken(self, token):
    #     return validateJws(token)


application = Application([UserManagementService], 'services.dbManager.soap',
                          in_protocol=Soap11(validator='lxml'),
                          out_protocol=Soap11())

wsgi_application = WsgiApplication(application)

if __name__ == '__main__':
    logging.basicConfig(level=logging.INFO)
    logging.getLogger('spyne.protocol.xml').setLevel(logging.INFO)

    logging.info("listening to http://127.0.0.1:8000")
    logging.info("wsdl is at: http://127.0.0.1:8000/?wsdl")

    server = make_server('127.0.0.1', 8000, wsgi_application)
    server.serve_forever()