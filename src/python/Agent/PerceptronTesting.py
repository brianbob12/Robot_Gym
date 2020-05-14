#FOR TESTING PURPOSES ONLY
from Network.Perceptron import Perceptron
myNet=Perceptron()
myNet.newNetwork(1,1,[1],["sigmoid"],0)
print(myNet.evaluate([[1.0]]))
for i in range (5):
    myNet.train([[1.0]],[[1]],1e-3)
    print(myNet.evaluate([[1.0]]))
