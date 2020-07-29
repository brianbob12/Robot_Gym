#FOR TESTING PURPOSES ONLY
#for now, a tool for creating random neural networks
import os
from Network.Perceptron import Perceptron
pathList=os.path.abspath("").split("\\")
pathList=pathList[:-2]
path=""
for pathL in pathList:
    path+=pathL+"\\"
path=path[:-1]
myNetA=Perceptron()
myNetA.importNetwork(path+"/playData/agents/a0/net1")
#myNetA.newNetwork(864,18,[256,128,64,32],["sigmoid","sigmoid","sigmoid","sigmoid","sigmoid"])
#print("startexport")
#myNetA.export("testNet")

