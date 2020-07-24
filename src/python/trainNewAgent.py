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

    try:
        options, arugments= getopt.getopt(args,"hi:o:e:")
    except getopt.GetoptError:
        print("trainNewAgent.py i- <inputDirecory> -o <outputDirectory> -e <experianceDirectory>")
        sys.exit(2)
    for opt, arg in options:
        print(opt,"-",arg)
        if opt =="-h":
            print("trainNewAgent.py -i <inputDirecory> -o <outputDirectory> -e <experianceDirectory>")
            sys.exit()
        elif opt=="-i":
            oldAgentDir=arg
        elif opt=="-o":
            newAgentDir=arg
        elif opt=="-e":
            experianceDir=arg

    #main code
    pathList=os.path.abspath("").split("\\")
    pathList=pathList[:-2]
    path=""
    for pathL in pathList:
        path+=pathL+"\\"
    path=path[:-1]

    agent=Agent(path+"/gameData/agentConfig.json")
    agent.importAgent(path+"/"+oldAgentDir)
    agent.trainAgent(path+"/"+experianceDir)




if __name__ == "__main__":
   main(sys.argv[1:])
