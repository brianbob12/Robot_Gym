#FOR TESTING PURPOSES ONLY
#for now, a tool for creating random neural networks
import os
from Network.Perceptron import Perceptron
myPath=os.path.abspath(__file__).split("\\")
myPath=myPath[:-4]
p=""
for i in myPath:
    p+=i
    p+="\\"
p=p[:-1]
myNetA=Perceptron()
myNetA.newNetwork(864,18,[256,128],["sigmoid","sigmoid","sigmoid"])
myNetA.export(p+"\\playData\\net1")
