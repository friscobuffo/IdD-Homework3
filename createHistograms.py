import matplotlib.pyplot as plt
import numpy as np
import csv
import os

def openDict(path):
	dictionary = dict()
	with open(path) as file:
		reader = csv.reader(file)
		for line in reader:
			if line == []:
				continue
			key, value = int(line[0]), int(line[1])
			dictionary[key] = value
		file.close()
		return dictionary

columnsNumber2tablesQuantity = openDict('stats/columnsNumber2tablesQuantity.csv')
rowsNumber2tablesQuantity = openDict('stats/rowsNumber2tablesQuantity.csv')
distinctValuesNumber2columnsQuantity = openDict('stats/distinctValuesNumber2columnsQuantity.csv')

path = "histograms"
if not os.path.exists(path):
	os.makedirs(path)
	   
x = np.array([key for key in columnsNumber2tablesQuantity for _ in range(columnsNumber2tablesQuantity[key])])
plt.hist(x, 20, (1,15))
plt.savefig("histograms/columnsNumber2tablesQuantity.jpg")
plt.clf()

x = np.array([key for key in rowsNumber2tablesQuantity for _ in range(rowsNumber2tablesQuantity[key])])
plt.hist(x, 80, (1,80))
plt.savefig("histograms/rowsNumber2tablesQuantity.jpg")
plt.clf()

x = np.array([key for key in distinctValuesNumber2columnsQuantity for _ in range(distinctValuesNumber2columnsQuantity[key])])
plt.hist(x, 80, (1,80))
plt.savefig("histograms/distinctValuesNumber2columnsQuantity.jpg")
plt.clf()
plt.close()