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
			key, value = int(line[0]), float(line[1])
			dictionary[key] = value
		file.close()
		return dictionary

folderAbsolutePath = str(__file__).removesuffix("createGraphsQuery.py")
graphsFolderAbsolutePath = folderAbsolutePath + "graphs/"
if not os.path.exists(graphsFolderAbsolutePath):
	os.makedirs(graphsFolderAbsolutePath)

statsFolderAbsolutePath = folderAbsolutePath + "stats/expand-column/"
columnSize2averageQueryTimeSeconds = openDict(statsFolderAbsolutePath + 'columnSize2averageQueryTimeSeconds.csv')
x = []
y = []
for columnSize in columnSize2averageQueryTimeSeconds:
	if (columnSize<500 and columnSize2averageQueryTimeSeconds[columnSize]<0.08): x.append(columnSize)
x = sorted(x)
for columnSize in x:
	y.append(columnSize2averageQueryTimeSeconds[columnSize])
plt.plot(x, y)
plt.savefig(graphsFolderAbsolutePath + "expandColumn-columnSize2averageQueryTimeSeconds.jpg")
plt.clf()

statsFolderAbsolutePath = folderAbsolutePath + "stats/merge-list/"
columnSize2averageQueryTimeSeconds = openDict(statsFolderAbsolutePath + 'columnSize2averageQueryTimeSeconds.csv')
x = []
y = []
for columnSize in columnSize2averageQueryTimeSeconds:
	x.append(columnSize)
x = sorted(x)
for columnSize in x:
	y.append(columnSize2averageQueryTimeSeconds[columnSize])
plt.plot(x, y)
plt.savefig(graphsFolderAbsolutePath + "mergeList-columnSize2averageQueryTimeSeconds.jpg")
plt.clf()

statsFolderAbsolutePath = folderAbsolutePath + "stats/expand-column/"
columnSize2averageQueryTimeSeconds = openDict(statsFolderAbsolutePath + 'columnSize2averageQueryTimeSeconds.csv')
x = []
y = []
for columnSize in columnSize2averageQueryTimeSeconds:
	if (columnSize<30 and columnSize2averageQueryTimeSeconds[columnSize]<0.08): x.append(columnSize)
x = sorted(x)
for columnSize in x:
	y.append(columnSize2averageQueryTimeSeconds[columnSize])
plt.plot(x, y)
plt.savefig(graphsFolderAbsolutePath + "expandColumnZoom-columnSize2averageQueryTimeSeconds.jpg")
plt.clf()

statsFolderAbsolutePath = folderAbsolutePath + "stats/merge-list/"
columnSize2averageQueryTimeSeconds = openDict(statsFolderAbsolutePath + 'columnSize2averageQueryTimeSeconds.csv')
x = []
y = []
for columnSize in columnSize2averageQueryTimeSeconds:
	if (columnSize<30): x.append(columnSize)
x = sorted(x)
for columnSize in x:
	y.append(columnSize2averageQueryTimeSeconds[columnSize])
plt.plot(x, y)
plt.savefig(graphsFolderAbsolutePath + "mergeListZoom-columnSize2averageQueryTimeSeconds.jpg")
plt.clf()
plt.close()