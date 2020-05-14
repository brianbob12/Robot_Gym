#
#
#@author cyrus singer
#Robot_Gym version 0.1
#
#

#dependencies
import tensorflow as tf
import numpy#this is actually a dependancy of tensorflow

#
#Network
#
#This class holds a single neural netowork with some parts of the training protocol.
#As well as export and import protocols.
#
#
 
class Network:
    # bare init function becuase of the option to import exsisting network from a folder
    def __init__(self):
        return

    def newNetwork(self,inputSize,outputSize,nHidden,activation):
        self.inputSize=inputSize
        self.outputSize=outputSize
        self.nHidden=nHidden
        self.activation=activation#a list of funcions lenght of nHidden+1

    #liniar function returns iput
    def liniar(x):
        return x
