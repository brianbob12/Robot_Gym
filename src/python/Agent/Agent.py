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

#
#Agent
#
#This class is a manager for the double DQN training used to train Agents in the game to do well in levels and mimic the player
# This will load or create a new Agent and manage training for it given data from files.
#


#this is quite obiously a work in progress
class Agent:

    switchRate=1#how training iterations before the agent switches to the other network

    def __init__(self):
        return

    #function to create a new agent
    def createNewAgent(self):

        #establish 2 neural networks for double deep Q learning
        self.network1=Net()
        self.network1.newNetwork(495,8,[256,128,64,32,16],["sigmoid","sigmoid","sigmoid","sigmoid","sigmoid","sigmoid"])
        self.network2=Net()
        self.network2.newNetwork(495,8,[256,128,64,32,16],["sigmoid","sigmoid","sigmoid","sigmoid","sigmoid","sigmoid"])

    #imports an agent from a path
    def importAgent(self,path):
        #establish 2 neural networks for double deep Q learning
        self.network1=Net()
        self.network1.importNetwork(path+"/net1")
        self.network2=Net()
        self.network2.importNetwork(path+"/net2")

    #exports the agent to the given path
    def export(self,path):
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
    def trainAgent(self,path):
        #iterate over all files
        print("test")
