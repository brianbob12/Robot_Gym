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
import getopt
#
#trainNewAgent
#
#This program will train a new agent from experiance
#
#The program takes arguments from the command line: directory of old agent, direcory of experiance, direcory of new agent

def main(args):
    oldAgentDir=""
    experianceDir=""
    newAgentDir=""
    trainingIterations=""

    try:
        options, arugments= getopt.getopt(args,"hi:o:e:l:")
    except getopt.GetoptError:
        print("trainNewAgent.py i- <inputDirecory> -o <outputDirectory> -e <experianceDirectory> -l <trainingIterations>")
        sys.exit(2)
    for opt, arg in options:
        if opt =="-h":
            print("trainNewAgent.py -i <inputDirecory> -o <outputDirectory> -e <experianceDirectory> -l <trainingIterations>")
            sys.exit()
        elif opt=="-i":
            oldAgentDir=arg
        elif opt=="-o":
            newAgentDir=arg
        elif opt=="-e":
            experianceDir=arg
        elif opt=="-l":
            trainingIterations=arg

    #main code
    pathList=os.path.abspath("").split("\\")
    pathList=pathList[:-2]
    path=""
    for pathL in pathList:
        path+=pathL+"\\"
    path=path[:-1]

    agent=Agent(path+"/gameData/agentConfig.json")
    agent.importAgent(path+"/"+oldAgentDir)
    agent.loadTrainingData(path+"/"+experianceDir)

    iter=1
    try:
        iter=int(trainingIterations)
    except:
        pass

    for i in range(iter):
        print("Training:",iter)
        agent.trainAgent()
    #export agent
    agent.export(path+"/"+newAgentDir)



if __name__ == "__main__":
   main(sys.argv[1:])
