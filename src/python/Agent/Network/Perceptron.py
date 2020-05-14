#
#
#@author cyrus singer
#Robot_Gym version 0.1
#
#

#dependencies
#only importing the bare minimum to save runtime
from tensorflow import (Variable,function,matmul,constant,GradientTape)#this line is too slow
from tensorflow.random import truncated_normal
from tensorflow.keras.regularizers import l2
from tensorflow.keras.optimizers import Adam
from tensorflow.nn import relu,sigmoid
import numpy#this is actually a dependancy of tensorflow

#
#Network
#
#This class holds a single neural netowork(MLP) with some parts of the training protocol.
#As well as export and import protocols.
# Trained using stocastic gradient decent(specifically Adam Optimiser).
#

class Perceptron:
    # bare init function becuase of the option to import exsisting network from a folder
    def __init__(self):
        return

    #create a new neural network based on hyperperameters given as arguments
    def newNetwork(self,inputSize,outputSize,nHidden,activation,L2val):
        self.inputSize=inputSize
        self.outputSize=outputSize
        self.nHidden=nHidden#a list of the number of neurons in each hidden layer
        self.activation=activation#a list of strings length of nHidden+1
        #initalise a map of string to function for activation fuctions
        self.activationLookup={"relu":relu,"liniar":self.liniar,"sigmoid":sigmoid}
        self.L2val=L2val

        #lists of weights and biases by layer
        self.weights=[]#list of tf.Variables
        self.biases=[]

        #bias initilisation value
        biasInit=0.1

        #for every other layer
        for i in range(len(nHidden)):#includes first hideen layer, excludes output layer
            self.biases.append(Variable(constant(biasInit,shape=[nHidden[i]])))
            self.weights.append(Variable(truncated_normal([nHidden[i-1],nHidden[i]],stddev=0.1)))
        #outputlayer stuff
        self.biases.append(Variable(constant(biasInit,shape=[outputSize])))
        self.weights.append(Variable(truncated_normal([nHidden[-1],outputSize],stddev=0.1)))

    #returns a list of pointers to trainable variables
    def getTrainableVariables(self):
        out=[]
        for i in range(len(self.nHidden)+1):
            out.append(self.weights[i])
            out.append(self.biases[i])
        return out

    #liniar function returns iput
    @function
    def liniar(self,x):
        return(x)

    #evaluates the network for a list of inputs
    @function
    def evaluate(self,x):
        #note: x has shape(batchsizse,inputSize)
        #ensure that layers are floats
        layerVals=[x]# a list of the neruon value for each x
        #[first set of input layer vaues,second set of input layer values]
        for i in range(len(self.nHidden)+1):#for each hidden layer and the output layer
            layerVals.append(matmul(layerVals[-1],self.weights[i])+self.biases[1])#I love Tensorflow 2!

        #return final layer as output layer
        return layerVals[-1]

    #train a nerual netwrok to fit the data provided
    def train(self,X,Y,learningRate):
        #apply L2 regularization to avoid overfitting
        #this is really really important
        regularizer=l2(self.L2val)#just ot be clear this is tf.keras.regularizers.l2
        regularizer(self.weights)

        #compute gradients of weights and biases
        with GradientTape() as g:
            for i in range(len(self.nHidden)+1):#iterate over layers
                g.watch(self.getTrainableVariables())

            #calculate error
            guess=self.evaluate(X)
            #calculate error using MSE
            error=0
            for i in range(len(Y)):
                error+=(guess[i]-Y[i])**2
            error=error/len(Y)

        optimizer=Adam(learningRate)
        grads=g.gradient(error,self.getTrainableVariables())
        optimizer.apply_gradients(zip(grads,self.getTrainableVariables()),)
