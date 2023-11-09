import matplotlib.pyplot as plt
import numpy as np
import csv
import os

def openDict(path):
	dictionary = dict()
	with open(path) as file:
		reader = csv.reader(file)
		for line in reader:
			if line == []: continue
			key, value = int(line[0]), int(line[1])
			dictionary[key] = value
		file.close()
		return dictionary

def openDictFloat(path):
	dictionary = dict()
	with open(path) as file:
		reader = csv.reader(file)
		for line in reader:
			if line == []: continue
			key, value = float(line[0]), int(line[1])
			dictionary[key] = value
		file.close()
		return dictionary

folderAbsolutePath = str(__file__).removesuffix("createHistograms.py")
statsFolderAbsolutePath = folderAbsolutePath + "stats/"

columnsNumber2tablesQuantity = openDict(statsFolderAbsolutePath + 'columnsNumber2tablesQuantity.csv')
rowsNumber2tablesQuantity = openDict(statsFolderAbsolutePath + 'rowsNumber2tablesQuantity.csv')
distinctValuesNumber2columnsQuantity = openDict(statsFolderAbsolutePath + 'distinctValuesNumber2columnsQuantity.csv')
tokensNumber2cellsQuantity = openDict(statsFolderAbsolutePath + 'tokensNumber2cellsQuantity.csv')
percentageRepeatedValues2columnsQuantity = openDictFloat(statsFolderAbsolutePath + 'percentageRepeatedValues2columnsQuantity.csv')

histogramsFolderAbsolutePath = folderAbsolutePath + "histograms/"
if not os.path.exists(histogramsFolderAbsolutePath):
	os.makedirs(histogramsFolderAbsolutePath)
	   
x = np.array([key for key in columnsNumber2tablesQuantity for _ in range(columnsNumber2tablesQuantity[key])])
plt.hist(x, 22, (1,22))
plt.savefig(histogramsFolderAbsolutePath + "columnsNumber2tablesQuantity.jpg")
plt.clf()

keys = list(percentageRepeatedValues2columnsQuantity)
myKeys = [0, 20, 40, 60, 80, 100]
labels = ["0", ">0  \n≤20", ">20\n≤40", ">40\n≤60", ">60\n≤80", ">80"]
myValues = []
for k in myKeys:
	myValue = 0
	for k2 in keys:
		if k2<=k:
			keys.remove(k2)
			myValue += percentageRepeatedValues2columnsQuantity.get(k2)
	myValues.append(myValue)
tot = sum(myValues)

values = [v/tot for v in myValues]
plt.pie(values, labels=labels)
plt.savefig(histogramsFolderAbsolutePath + "percentageRepeatedValues2columnsQuantity.jpg")
plt.clf()

x = np.array([key for key in rowsNumber2tablesQuantity for _ in range(rowsNumber2tablesQuantity[key])])
plt.hist(x, 80, (1,80))
plt.savefig(histogramsFolderAbsolutePath + "rowsNumber2tablesQuantity.jpg")
plt.clf()

x = np.array([key for key in distinctValuesNumber2columnsQuantity for _ in range(distinctValuesNumber2columnsQuantity[key])])
plt.hist(x, 60, (1,60))
plt.savefig(histogramsFolderAbsolutePath + "distinctValuesNumber2columnsQuantity.jpg")
plt.clf()

x = np.array([key for key in tokensNumber2cellsQuantity for _ in range(tokensNumber2cellsQuantity[key])])
plt.hist(x, 60, (1,60))
plt.savefig(histogramsFolderAbsolutePath + "tokensNumber2cellsQuantity.jpg")
plt.clf()

plt.close()