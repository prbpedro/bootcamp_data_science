"""
Created on Mon Jul 16 12:48:23 2020

@author: prbpedro
"""
import pandas as pd
from matplotlib import pyplot as plt
import numpy as np

def preprocess(dataframe):
    dataframe = dataframe[['Age', 'Age1stCode', 'ConvertedComp', 'Country', 'JobSat', 'OpSys', 'YearsCode', 'EdLevel']]
    return dataframe

def plot_mean_salary_per_edlevel_and_country(dataframe, country):
    dfplot = dataframe[['Country','ConvertedComp','EdLevel']]
    dfplot = dfplot.dropna()
    dfplot = dfplot[dfplot['Country'] == country]
    dfplot.drop(['Country'], axis=1, inplace=True)
    dfplot = dfplot.groupby(['EdLevel']).mean()
    dfplot = dfplot.sort_values(by=['ConvertedComp'], ascending=False)
    dfplot.plot.bar()
    plt.show()
    
def plot_mean_salary_per_jobsat_and_country(dataframe, country):
    dfplot = dataframe[['Country','ConvertedComp','JobSat']]
    dfplot = dfplot.dropna()
    dfplot = dfplot[dfplot['Country'] == country]
    dfplot.drop(['Country'], axis=1, inplace=True)
    dfplot = dfplot.groupby(['JobSat']).mean()
    dfplot = dfplot.sort_values(by=['ConvertedComp'], ascending=False)
    dfplot.plot.bar()
    plt.show()

def plot_opsys_per_country(dataframe, country):
    dfplot = dataframe[['Country','OpSys']]
    dfplot = dfplot.dropna()
    dfplot = dfplot[dfplot['Country'] == country]
    dfplot.drop(['Country'], axis=1, inplace=True)
    dfplot = dfplot['OpSys'].value_counts()
    dfplot = dfplot.sort_values(ascending=False)
    dfplot.plot.bar()
    plt.show()

def plot_mean_salary_per_age_and_country(dataframe, country):
    dfplot = dataframe[['Country','ConvertedComp','Age']]
    dfplot = dfplot.dropna()
    dfplot = dfplot[dfplot['Country'] == country]
    dfplot.drop(['Country'], axis=1, inplace=True)
    dfplot = dfplot.groupby(['Age']).mean()
    dfplot = dfplot.sort_values(by=['ConvertedComp'], ascending=False)
    dfplot = dfplot.reset_index()
    dfplot.plot.scatter(x="Age", y="ConvertedComp")
    x = dfplot['Age'].values
    y = dfplot['ConvertedComp'].values
    z = np.polyfit(x, y, 1)
    p = np.poly1d(z)
    plt.plot(x,p(x),"r--")
    plt.show()
    
def print_meadian_mean_salary_per_country(dataframe, country):
    dfplot = dataframe[['Country','ConvertedComp']]
    dfplot = dfplot.dropna()
    dfplot = dfplot[dfplot['Country'] == country]
    dfplot.drop(['Country'], axis=1, inplace=True)
    print(dfplot.describe(), end='\n\n')
    
def print_country_no_education_dev(dataframe, countryex):
    dfplot = dataframe[['Country','EdLevel']]
    dfplot = dfplot.dropna()
    dfplot = dfplot[dfplot['Country'] != countryex]
    dfplot = dfplot[dfplot['EdLevel'] == 'I never completed any formal education']
    print(dfplot['Country'].value_counts(), end='\n\n')
    
def print_age_start_per_country(dataframe, country):
    dfplot = dataframe[['Country','Age1stCode']]
    dfplot = dfplot.dropna()
    dfplot = dfplot[dfplot['Country'] == country]
    dfplot.drop(['Country'], axis=1, inplace=True)
    print(dfplot['Age1stCode'].value_counts(), end='\n\n')
    
caminho_base='/home/prbpedro/Development/repositories/github/bootcamp_data_science/data/'
dataframe=pd.read_csv(caminho_base + 'survey_results_public.csv')

dataframe = preprocess(dataframe)
plot_mean_salary_per_edlevel_and_country(dataframe, 'France')
plot_mean_salary_per_jobsat_and_country(dataframe, 'Chile')
plot_opsys_per_country(dataframe, 'France')
plot_mean_salary_per_age_and_country(dataframe, 'Albania')
print_meadian_mean_salary_per_country(dataframe, 'Brazil')
print_country_no_education_dev(dataframe, 'United States')
print_age_start_per_country(dataframe, 'Brazil')