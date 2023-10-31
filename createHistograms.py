import matplotlib.pyplot as plt
import numpy as np
import csv
import os

columnsNumber2tablesQuantity = dict()
with open('stats/columnsNumber2tablesQuantity.csv') as file:
	reader = csv.reader(file)
	for line in reader:
		if line == []:
			continue
		columnsNumber, tablesQuantity = int(line[0]), int(line[1])
		columnsNumber2tablesQuantity[columnsNumber] = tablesQuantity
	file.close()

rowsNumber2tablesQuantity = dict()
with open('stats/rowsNumber2tablesQuantity.csv') as file:
	reader = csv.reader(file)
	for line in reader:
		if line == []:
			continue
		rowsNumber, tablesQuantity = int(line[0]), int(line[1])
		rowsNumber2tablesQuantity[rowsNumber] = tablesQuantity
	file.close()
	
distinctValuesNumber2columnsQuantity = dict()
with open('stats/distinctValuesNumber2columnsQuantity.csv') as file:
	reader = csv.reader(file)
	for line in reader:
		if line == []:
			continue
		distinctValues, columnsQuantity = int(line[0]), int(line[1])
		distinctValuesNumber2columnsQuantity[distinctValues] = columnsQuantity
	file.close()

path = "histograms"
if not os.path.exists(path):
	   os.makedirs(path)
	   
temp = [key for key in columnsNumber2tablesQuantity for _ in range(columnsNumber2tablesQuantity[key])]
array = np.array(temp)
fig = plt.figure
plt.hist(array, 20, (1,15))
plt.savefig("histograms/columnsNumber2tablesQuantity.jpg")
plt.clf()

temp = [key for key in rowsNumber2tablesQuantity for _ in range(rowsNumber2tablesQuantity[key])]
array = np.array(temp)
plt.hist
plt.hist(array, 80, (1,80))
plt.savefig("histograms/rowsNumber2tablesQuantity.jpg")
plt.clf()

temp = []
for key in distinctValuesNumber2columnsQuantity:
	for _ in range(distinctValuesNumber2columnsQuantity.get(key)):
		temp.append(key)	
array = np.array(temp)
plt.hist
plt.hist(array, 80, (1,80))
plt.savefig("histograms/distinctValuesNumber2columnsQuantity.jpg")
plt.clf()