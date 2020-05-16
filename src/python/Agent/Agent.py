#
#
#@author cyrus singer
#Robot_Gym version 0.1
#
#

try:
    from Network.Perceptron import Perceptron as Net
    n=Net()
    print(n)
except Exception as e:
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
    def __init__(self):
        return
    #function to create a new agent
    def createNewAgent(self):

        #establish 2 neural networks for double deep Q learning
        self.network1=Net(495,8,[256,128,64,32,16],["sigmoid","sigmoid","sigmoid","sigmoid","sigmoid","sigmoid"])
        self.network2=Net(495,8,[256,128,64,32,16],["sigmoid","sigmoid","sigmoid","sigmoid","sigmoid","sigmoid"])
        
