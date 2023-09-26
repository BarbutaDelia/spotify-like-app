from suds.client import Client
if __name__ == "__main__":
    c = Client('http://localhost:8000/?wsdl')
    # print(c.service.createUser("gabriela", "gabriela", "client", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjc0NDA5MzIxLCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjgwMDAiLCJqdGkiOiIxOTc1Y2MwZS1hMmI5LTQxOTAtYjY4ZC02YjY0MjRkMjg5ZjYiLCJyb2xlIjpbImFkbWluaXN0cmF0b3IgYXBsaWNhdGllIl19.dbezml74WlbyitBi5MY2tUyuE6b9JTkNs1XyrTWqhiE"))
    # print(c.service.changePassword("test1", "test2"))
    # print(c.service.addRole("test", "content manager"))
    print(c.service.getInfo(1, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjcyNjgxODMyLCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjgwMDAiLCJqdGkiOiI0ZTFiYzU3MC01OGY3LTQyYzEtYmMyYS1hYTRkMWY0NzhiMTAiLCJyb2xlIjpbImFkbWluaXN0cmF0b3IgYXBsaWNhdGllIl19.urrCMm8eye_sBeVjRcb_5exJyWLoJcz8Bixp-GP9IKU"))
    print(c.service.getRoles("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjcyNjgxODMyLCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjgwMDAiLCJqdGkiOiI0ZTFiYzU3MC01OGY3LTQyYzEtYmMyYS1hYTRkMWY0NzhiMTAiLCJyb2xlIjpbImFkbWluaXN0cmF0b3IgYXBsaWNhdGllIl19.urrCMm8eye_sBeVjRcb_5exJyWLoJcz8Bixp-GP9IKU"))
    print(c.service.getUsers("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjcyNzU1NjEwLCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjgwMDAiLCJqdGkiOiI4MWJhNDlhMC01OGUwLTQ0ODItYmYwNy1hMTdlNzIwODE4OTYiLCJyb2xlIjpbImFkbWluaXN0cmF0b3IgYXBsaWNhdGllIl19.9GVwd_e2rPtfLY27WhyXpXFfV9Dwuh7cB6iOJL6dtrs"))
    print(c.service.getUsers("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjcyNjgxODMyLCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjgwMDAiLCJqdGkiOiI0ZTFiYzU3MC01OGY3LTQyYzEtYmMyYS1hYTRkMWY0NzhiMTAiLCJyb2xlIjpbImFkbWluaXN0cmF0b3IgYXBsaWNhdGllIl19.urrCMm8eye_sBeVjRcb_5exJyWLoJcz8Bixp-GP9IKU"))
    print(c.service.verifyCredentials("gabi", "gabi"))
    # print(c.service.changePassword("1", "delia", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjcyNjc4MDkzLCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjgwMDAiLCJqdGkiOiJkOTZiODJlZi04MDIyLTQzNjUtOTYwMy1iYzcxZDYwYjFmMGIiLCJyb2xlIjpbImFkbWluaXN0cmF0b3IgYXBsaWNhdGllIl19.078NjBd4RzwsbywfbRHlUOtjWSqsEqfDjlmDvXUHOUk"))
    # print(c.service.authorize("gabi", "client"))
    # print(c.service.logout("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzIiwiZXhwIjoxNjcyMjY3ODU1LCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjgwMDAiLCJqdGkiOiI5MDRkMzg2My1hZjNjLTQ5OWEtODU1Mi02OTAzOTFjZDRjOWQiLCJyb2xlIjpbImNvbnRlbnQgbWFuYWdlciJdfQ.Pmg37SXuEs_G6iTo2idzac1B2sDxDA1cGYrdhhFfT98"))
    # print(c.service.validateToken("eyJhbGciOiJIUzI1NiIdInR5cCI6IkpXVCJ9.eyJzdWIiOiIzIiwiZXhwIjoxNjcyMjU5NDU0LCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjgwMDAiLCJqdGkiOiI3OTZmMTNiNi00OGUxLTQ5NGEtYjgyNC04MjAzZjM4ZDhlZWQiLCJyb2xlIjoiWydjb250ZW50IG1hbmFnZXInXSJ9.JsMPiuQM9-_oaCF0SbauqdQ8LhnuknxUl5vvx0-SJIk"))
    # print(c.service.authorize("eyJhbGciOiJIUzI1NiIdInR5cCI6IkpXVCJ9.eyJzdWIiOiIzIiwiZXhwIjoxNjcyMjU5NDU0LCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjgwMDAiLCJqdGkiOiI3OTZmMTNiNi00OGUxLTQ5NGEtYjgyNC04MjAzZjM4ZDhlZWQiLCJyb2xlIjoiWydjb250ZW50IG1hbmFnZXInXSJ9.JsMPiuQM9-_oaCF0SbauqdQ8LhnuknxUl5vvx0-SJIk"))
    # print(c.service.createUser("ana", "ana", "content manager", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjcyNzU1NTgwLCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjgwMDAiLCJqdGkiOiIxZGMyNzhjNi05Y2UzLTQ2ZDUtYTE3ZS1iYTM4YzBmYjQ5MGYiLCJyb2xlIjpbImFkbWluaXN0cmF0b3IgYXBsaWNhdGllIl19.YbfG8V9C-El39dJvfltW3tSvAG2owNyZuGEbbGsVoPE"))

    # print(c.service.createUser("test1", "test1"))
    # print(c.service.substraction(10, 5))