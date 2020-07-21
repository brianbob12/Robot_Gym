#
#
#@author cyrus singer
#Robot_Gym version 0.1
#
#

try:
    from Network.Perceptron import Perceptron as Net
except Exception as e:
    #missing file
    print(e)
    exit()

import json
import os

#
#Agent
#
#This class is a manager for the double DQN training used to train Agents in the game to do well in levels and mimic the player
# This will load or create a new Agent and manage training for it given data from files.
#


#this is quite obiously a work in progress
class Agent:

    #self.config is a dictionary holding the operational information for the agent

    #takes a configuration path for a json file for settings
    def __init__(self,configPath):#throws file not found error
        with open(configPath) as file:
            self.config=json.load(file)
        return

    #function to create a new agent
    def createNewAgent(self):

        #establish 2 neural networks for double deep Q learning
        self.network1=Net()
        self.network1.newNetwork(864,18,[256,128,64,32],["sigmoid","sigmoid","sigmoid","sigmoid","sigmoid"])
        #network2 is a deepcopy of network1
        self.network2=self.network1.deepcopy()

    #imports an agent from a path
    def importAgent(self,path):
        #establish 2 neural networks for double deep Q learning
        self.network1=Net()
        self.network1.importNetwork(path+"/net1")
        self.network2=Net()
        self.network2.importNetwork(path+"/net2")

    #exports the agent to the given path
    def export(self,path):#throws missing key if bad config file
        import os
        try:
            os.mkdir(path)
        except FileExistsError:
            pass
        except Exception as e:
            raise(badPath(path))
        #save networks to path
        self.network1.export(path+"/net1")
        self.network2.export(path+"/net2")

    #trains the agent networks
    #inputs a directory of a list of observation filled text files
    #https://arxiv.org/pdf/1509.06461.pdf
    def trainAgent(self,path):
        data=[]#list of level data
        #equate networks
        self.network2=self.network1.deepcopy()#this does not have to be done every time
        #for each peice of level data there is a 2 dimentional float list [state,action,reward,state prime, action prime]
        for file in os.listdir(path):#iterate over all files
            #get data from files
            data.append([])
            #read from file
            with open(path+"/"+file,"r") as f:
                 lines=f.readlines()

            for line in lines:
                data[-1].append([part.split(",") for part in line.split(";")])

        #setup training training data
        #setup arguments
        x=[]#list of inputs
        y=[]#list of target* values (it is a moving target that will become more accurate as training progresses)
        yi=[]#for each training example the index of y[exapmle] that should be trained on

        #iterate over all level examples
        #NOTE: the network only choses one action
        #this is all done as one batch but it does not have to be this way
        for level in data:
            for observation in level:#observation is [state,action,reward,nextState,nextAction]
                x.append(observation[0])# add state to x
                target=reward+self.config["gamma"]*self.network2.evaluate([observation[3]])[observation[4]]
                yVal=[0]*self.network1.outputSize
                yVal[observation[1]]=target#reasighn selected action to target
                y.append([i for i in yVal])#this is because we want a new copy of yVal in place of a pointer
                yi.append(observation[1])#this means we will only be fitting the output for this action
                #Q(state,action,net1weights)= reward + gamma*Q(nextState,nextAction,net2weights) THIS IS WHAT THE GOAL IS

        #TRAINING PROCEDURE
        for i in range(self.config["iterationsPerGame"]):
            #actually train the network
            self.network1.train(x,y,yi,self.config["learningRate"],self.config["l2val"])
