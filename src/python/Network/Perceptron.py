#
#
#@author cyrus singer
#Robot_Gym version 0.1
#
#

#dependencies
#only importing the bare minimum to save runtime
from tensorflow import (Variable,function,matmul,constant,GradientTape,ones)#this line is too slow
from tensorflow.random import truncated_normal
from tensorflow.keras.regularizers import l2
from tensorflow.keras.optimizers import Adam
from tensorflow.nn import relu,sigmoid
from tensorflow.math import tanh
from tensorflow import function
import numpy#this is actually a dependancy of tensorflow
import struct#for export and import
from .Exceptions import *

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
        #initalise a map of string to function for activation fuctions
        self.activationLookup={"relu":relu,"linear":self.linear,"sigmoid":sigmoid,"tanh":tanh}

    #create a new neural network based on hyperperameters given as arguments
    def newNetwork(self,inputSize,outputSize,nHidden,activation):
        self.inputSize=inputSize
        self.outputSize=outputSize
        self.nHidden=nHidden#a list of the number of neurons in each hidden layer
        self.activation=activation#a list of strings length of nHidden+1

        #pre-network safety checks(passing erros)
        #whilst error handeling is not done here it is usefull to pass erros on to the next program down the line
        if(len(activation)!=len(nHidden)+1):
            raise(unspecifiedActivation)#see Network.Exceptions
        for i in activation:
            if not i in self.activationLookup.keys():
                raise(unknownActivationFunction(i))


        #lists of weights and biases by layer
        self.weights=[]#list of tf.Variables
        self.biases=[]

        #bias initilisation value
        biasInit=0.1

        #for the first hidden layer
        self.biases.append(Variable(constant(biasInit,shape=[nHidden[0]])))
        self.weights.append(Variable(truncated_normal([inputSize,nHidden[0]],stddev=0.1)))
        #for every other layer
        for i in range(1,len(nHidden)):#excludes first hideen layer, excludes output layer
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

    #liniar function returns input
    @function
    def linear(self,x):
        return(x)

    #evaluates the network for a list of inputs
    #forward propagation
    @function
    def evaluate(self,x):
        #print(x)#for debug
        #note: x has shape(batchsizse,inputSize)
        #ensure that layers are floats
        layerVals=[x]# a list of the neruon value for each x
        #[first set of input layer vaues,second set of input layer values]
        for i in range(len(self.nHidden)+1):#for each hidden layer and the output layer
            layerVals.append(self.activationLookup[self.activation[i]](matmul(layerVals[-1],self.weights[i])+self.biases[i]))#I love Tensorflow 2!
        #return final layer as output layer
        return layerVals[-1]

    #train a nerual netwrok to fit the data provided
    #returns squared error
    #only trianes for one Yindex(Yi) per training example
    def train(self,X,Y,Yi,learningRate,L2val):

        #very sorry L2 is currtently out of order I will fix this later

        #apply L2 regularization to avoid overfitting
        #this is really really important
        #regularizer=l2(L2val)#just to be clear this is tf.keras.regularizers.l2
        #regularizer(self.weights)

        #compute gradients of weights and biases
        with GradientTape() as g:
            for i in range(len(self.nHidden)+1):#iterate over layers
                g.watch(self.getTrainableVariables())

            #calculate error
            guess=self.evaluate(X)
            #calculate error using sqared error
            error=0
            for i in range(len(Y)):
                error+=(guess[i][Yi[i]]-Y[i][Yi[i]])**2
            error=error/len(Y)

        optimizer=Adam(learningRate)
        grads=g.gradient(error,self.getTrainableVariables())
        optimizer.apply_gradients(zip(grads,self.getTrainableVariables()),)
        return error

    #export currently loaded network to file
    def export(self,path):#throws IOerror
        import os
        try:
            os.mkdir(path)
        except FileExistsError:
            pass
        except Exception as e:
            raise(badPath(path))



        #store hyperperanmeters in hyper.txt
        with open(path+"\\hyper.txt","w") as f:
            f.write(str(self.inputSize)+"\n")
            f.write(str(self.outputSize)+"\n")
            for i,v in enumerate(self.nHidden):
                f.write(str(v))
                if(i+1!=len(self.nHidden)):
                    f.write(",")
            f.write("\n")
            for i,v in enumerate(self.activation):
                f.write(v)
                if(i+1!=len(self.activation)):
                    f.write(",")
            f.write("\n")


        #set weight and bias files as comma sperated values
        #note: some percision will be lost when converting binary floats to strings
        for i in range(len(self.nHidden)+1):
            out=[]
            with open(path+"\\w"+str(i)+".weights","wb") as f:
                for j in range(self.weights[i].get_shape()[0]):
                    for k in range(self.weights[i][j].get_shape()[0]):
                        out.append(float(self.weights[i][j][k]))
                f.write(bytearray(struct.pack(str(len(out))+"f",*out)))
            out=[]
            with open(path+"\\b"+str(i)+".biases","wb") as f:
                for j in range(self.biases[i].get_shape()[0]):
                    out.append(float(self.biases[i][j]))
                f.write(bytearray(struct.pack(str(len(out))+"f",*out)))

    #import a network of the format given above
    def importNetwork(self,path):#throws IOerror and byte error
        #check if the path is real
        import os
        if(not os.path.exists(path)):
            raise(badPath(path))

        #check hyperperameters
        try:
            with open(path+"\\hyper.txt","r") as f:
                hyperPerameters=f.readlines()
        except IOError:
            raise(missingFile(path+"\\hyper.txt"))
        try:
            self.inputSize=int(hyperPerameters[0])
            self.outputSize=int(hyperPerameters[1])
            self.nHidden=[int(i) for i in hyperPerameters[2].split(",")]
            self.activation=hyperPerameters[3][:-1].split(",")#exclude final \n
        except:
            raise(fileMissingData(path+"\\hyper.txt"))

        #pre-network safety checks(passing erros)
        if(len(self.activation)!=len(self.nHidden)+1):
            raise(unspecifiedActivation)#see Network.Exceptions
        for i in self.activation:
            if not i in self.activationLookup.keys():
                raise(unknownActivationFunction(i))

        #initalise variables
        self.biases=[]
        self.weights=[]
        #I know this is a little messy. It is the same segment three times
        try:
            with open(path+"\\w0.weights","rb") as f:
                raw=f.read()#type of bytes
                inp=struct.unpack(str(self.inputSize*self.nHidden[0])+"f",raw)#list of int
                tad=[]
                for j in range(self.inputSize):
                    tad2=[]
                    for k in range(self.nHidden[0]):
                        tad2.append(inp[j*self.nHidden[0]+k])
                    tad.append(tad2)
                self.weights.append(Variable(tad))
        except IOError:
            raise(missingFile(path,path+"\\w0.weights"))
        try:
            with open(path+"\\b0.biases","rb") as f:
                raw=f.read()#type of bytes
                inp=struct.unpack(str(self.nHidden[0])+"f",raw)#list of floats
                self.biases.append(Variable([i for i in inp]))

        except IOError:
            raise(missingFile(path,path+"\\b0.bases"))

        for i in range(1,len(self.nHidden)):
            try:
                with open(path+"\\w"+str(i)+".weights","rb") as f:
                    raw=f.read()#type of bytes
                    #print(path+"\\w"+str(i)+".weights")#DEBUG
                    inp=struct.unpack(str(self.nHidden[i-1]*self.nHidden[i])+"f",raw)#list of int
                    tad=[]
                    for j in range(self.nHidden[i-1]):
                        tad2=[]
                        for k in range(self.nHidden[i]):
                            tad2.append(inp[j*self.nHidden[i]+k])
                        tad.append(tad2)
                    self.weights.append(Variable(tad))

            except IOError:
                raise(missingFile(path,path+"\\w"+str(i)+".weights"))

            try:
                with open(path+"\\b"+str(i)+".biases","rb") as f:
                    raw=f.read()#type of bytes
                    inp=struct.unpack(str(self.nHidden[i])+"f",raw)#list of int
                    self.biases.append(Variable([i for i in inp]))

            except IOError:
                raise(missingFile(path,path+"\\b"+str(i)+".bases"))

        try:
            with open(path+"\\w"+str(len(self.nHidden))+".weights","rb") as f:
                raw=f.read()#type of bytes
                inp=struct.unpack(str(self.nHidden[-1]*self.outputSize)+"f",raw)#list of int
                tad=[]
                for j in range(self.nHidden[-1]):
                    tad2=[]
                    for k in range(self.outputSize):
                        tad2.append(inp[j*self.outputSize+k])
                    tad.append(tad2)
                self.weights.append(Variable(tad))

        except IOError:
            raise(missingFile(path,path+"\\w"+str(len(self.nHidden))+".weights"))
        try:
            with open(path+"\\b"+str(len(self.nHidden))+".biases","rb") as f:
                raw=f.read()#type of bytes
                inp=struct.unpack(str(self.outputSize)+"f",raw)#list of int
                self.biases.append(Variable([i for i in inp]))

        except IOError:
            raise(missingFile(path,path+"\\b"+str(len(self.nHidden))+".bases"))

    #return deepcopy of self
    def deepcopy(self):

        import copy

        out=Perceptron()
        out.inputSize=self.inputSize#int so no copy needed
        out.outputSize=self.outputSize#int so no copy needed
        out.nHidden=copy.deepcopy(self.nHidden)
        out.activation=copy.deepcopy(self.activation)

        try:
            out.weights=copy.deepcopy(self.weights)
            out.biases=copy.deepcopy(self.biases)
        except:
            #weights and biases not initialized
            pass

        return out
