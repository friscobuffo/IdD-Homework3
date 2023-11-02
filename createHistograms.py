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

folderAbsolutePath = str(__file__).removesuffix("createHistograms.py")
statsFolderAbsolutePath = folderAbsolutePath + "stats/"

columnsNumber2tablesQuantity = openDict(statsFolderAbsolutePath + 'columnsNumber2tablesQuantity.csv')
rowsNumber2tablesQuantity = openDict(statsFolderAbsolutePath + 'rowsNumber2tablesQuantity.csv')
distinctValuesNumber2columnsQuantity = openDict(statsFolderAbsolutePath + 'distinctValuesNumber2columnsQuantity.csv')

histogramsFolderAbsolutePath = folderAbsolutePath + "histograms/"
if not os.path.exists(histogramsFolderAbsolutePath):
	os.makedirs(histogramsFolderAbsolutePath)
	   
x = np.array([key for key in columnsNumber2tablesQuantity for _ in range(columnsNumber2tablesQuantity[key])])
plt.hist(x, 22, (1,22))
plt.savefig(histogramsFolderAbsolutePath + "columnsNumber2tablesQuantity.jpg")
plt.clf()

x = np.array([key for key in rowsNumber2tablesQuantity for _ in range(rowsNumber2tablesQuantity[key])])
plt.hist(x, 80, (1,80))
plt.savefig(histogramsFolderAbsolutePath + "rowsNumber2tablesQuantity.jpg")
plt.clf()

x = np.array([key for key in distinctValuesNumber2columnsQuantity for _ in range(distinctValuesNumber2columnsQuantity[key])])
plt.hist(x, 60, (1,60))
plt.savefig(histogramsFolderAbsolutePath + "distinctValuesNumber2columnsQuantity.jpg")
plt.clf()
plt.close()