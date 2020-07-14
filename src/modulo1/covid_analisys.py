"""
Created on Mon Jul 13 13:28:23 2020

@author: prbpedro
"""
import pandas as pd
from matplotlib import pyplot as plt
import matplotlib.dates as mdates
import numpy as np
import requests

countrys_lat_long = {}
def get_latlong_country(country):
    global countrys_lat_long
    if countrys_lat_long.get(country) is None:
        
        url = '{0}{1}{2}'.format('http://nominatim.openstreetmap.org/search?country=',
                                 country,
                                 '&format=json&polygon=0')
        print(country)
        responselist = requests.get(url).json()
        if len(responselist) > 0:
            response = responselist[0]
            lst = [response.get(key) for key in ['lat','lon']]
            output = [float(i) for i in lst]
            countrys_lat_long[country] = output
        else:
            countrys_lat_long[country] = [None, None]
            
def getlat(c):
    return countrys_lat_long[c][0]

def getlong(c):
    return countrys_lat_long[c][1]

def preprocess(dataframe):
    dataframe['Country/Region'] = dataframe['Country/Region'].replace(np.nan, '').astype('category')
    dataframe['Province/State'] = dataframe['Province/State'].replace(np.nan, '').astype('category')
    dataframe['ObservationDate'] = pd.to_datetime(dataframe['ObservationDate'])
    dataframe['Last Update'] = pd.to_datetime(dataframe['Last Update'])
    dataframe['Active'] = dataframe['Confirmed'] - (dataframe['Recovered'] + dataframe['Deaths'])
    
    dataframe['Country/Region'] = dataframe['Country/Region'].replace(' Azerbaijan', 'Azerbaijan')
    dataframe['Country/Region'] = dataframe['Country/Region'].replace('Mainland China', 'China')
    dataframe['Country/Region'] = dataframe['Country/Region'].replace('(\'St. Martin\',)', 'St. Martin')
    dataframe['Country/Region'] = dataframe['Country/Region'].replace('occupied Palestinian territory', 'Palestinian')
    #for c in dataframe['Country/Region']:
        #get_latlong_country(c)
    
    #dataframe["lat"] = dataframe['Country/Region'].apply(getlat)
    #dataframe["long"] = dataframe['Country/Region'].apply(getlong)
    dataframe.dropna(inplace=True)


def plotlinechart(dataframe):
    dfplot = dataframe[dataframe['Country/Region'] == 'China']
    dfplot = dfplot.groupby(['ObservationDate']).sum()
    dfplot.drop(['SNo'], axis=1, inplace=True)
    f = plt.figure()
    plt.ticklabel_format(style = 'plain')
    plt.title('covid-19 evolution')
    ax = f.gca()
    dfplot.plot(kind='line', ax=ax)
    ax.xaxis.set_major_locator(mdates.MonthLocator())
    ax.xaxis.set_major_formatter(mdates.DateFormatter('%b %d'))
    ax.set_xlabel('')

caminho_base='/home/prbpedro/Development/repositories/github/bootcamp_data_science/data/'
dataframe=pd.read_csv(caminho_base + 'covid_19_data.csv')

preprocess(dataframe)
plotlinechart(dataframe)
dataframe.info()