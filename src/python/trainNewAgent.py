#
#
#@author cyrus singer
#Robot_Gym version 0.1
#
#

try:
    from Agent.Agent import Agent
    from Network.Exceptions import *
except Exception as e:
    #missing file
    print(e)
    exit()

import os.path
import sys
#
#trainNewAgent
#
#This program will train a new agent from experiance
#
#The program takes arguments from the command line: directory of old agent, direcory of experiance, direcory of new agent

#check for correct number of arguments
if(len(sys.argv)>4):#NOTE sys.argv indludes trainNewAgent.py as first argument
    print("Too few arguments")
    exit()

pathList=os.path.abspath("").split("\\")
pathList=pathList[:-2]
path=""
for pathL in pathList:
    path+=pathL+"\\"
path=path[:-1]

agent=Agent(path+"/gameData/agentConfig.json")
agent.createNewAgent()
#agent.export(path+"/playData/agents/a1")
