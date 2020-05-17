#FOR TESTING PURPOSES ONLY
import os
from Network.Perceptron import Perceptron
myPath=os.path.abspath(__file__).split("\\")
myPath=myPath[:-4]
p=""
for i in myPath:
    p+=i
    p+="\\"
p=p[:-1]
myNet=Perceptron()
#myNet.newNetwork(1,1,[5,5],["sigmoid","sigmoid","sigmoid"])
myNet.importNetwork(p+"\\playData\\net1")
print(myNet.evaluate([[1.0]]))
for i in range (0):
    myNet.train([[1.0]],[[1]],1e-3,0)
    print(myNet.evaluate([[1.0]]))
#myNet.export(p+"\\net1")
