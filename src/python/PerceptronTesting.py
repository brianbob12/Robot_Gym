#FOR TESTING PURPOSES ONLY
#for now, a tool for creating random neural networks
import os
from Network.Perceptron import Perceptron
myNetA=Perceptron()
#myNetA.newNetwork(864,18,[256,128,64,32],["sigmoid","sigmoid","sigmoid","sigmoid","sigmoid"])

pathList=os.path.abspath("").split("\\")
pathList=pathList[:-2]
path=""
for pathL in pathList:
    path+=pathL+"\\"
path=path[:-1]

myNetA.importNetwork(path+"/playData/net1")

print(myNetA.evaluate([[0.0]*864]))
print(myNetA.evaluate([[1.0]*864]))

for i in range(1):
    print()
    print()
    myNetA.train([[0.0]*864],[[1.0]*18],[0],0.1,0.1)

myNetA.export("testNet")
#    print(myNetA.evaluate([[0.0]]))
#    print(myNetA.evaluate([[1.0]]))
#    print(myNetA.weights)
    
