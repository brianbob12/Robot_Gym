import tensorflow as tf
import numpy
class Network:
    #input main hyperperameters of the network
    def __init__(self,inputSize,outputSize,nHidden,activation):
        self.inputSize=inputSize
        self.outputSize=outputSize
        self.nHidden=nHidden
        self.activation=activation#a list of funcions lenght of nHidden+1

    #liniar function returns iput
    def liniar(x):
        return x
