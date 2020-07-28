from Agent.Agent import Agent
from Network.Exceptions import *
import os.path

pathList=os.path.abspath("").split("\\")
pathList=pathList[:-2]
path=""
for pathL in pathList:
    path+=pathL+"\\"
path=path[:-1]

agent=Agent(path+"/gameData/agentConfig.json")
agent.createNewAgent()
agent.export(path+"/playData/agents/a0")
