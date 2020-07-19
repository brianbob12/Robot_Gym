#
#
#@author cyrus singer
#Robot_Gym version 0.1
#
#

#
#Netork.Exceptions
#
#This class holds custom Exceptions used for error handeling with the Perceptron object at Network.Perceptron.Perceptron
#

class unspecifiedActivation (Exception):
    pass

class unknownActivationFunction(Exception):
    def __init__(self,badVal):
        self.badValue=badVal

class badPath(Exception):
    def __init__(self,badPath):
        self.badPath=badPath

class missingFile(Exception):
    def __init__(self,path,file):
        self.path=path
        self.fileName=file

class fileMissingData(Exception):
    def __init__(self,file):
        self.filePath=file
