"""
Created on Tue Ago 25 16:35:50 2020
@author: Pedro Ribeiro Baptista
"""

import pandas as pd

caminho_base='/home/prbpedro/Development/repositories/github/bootcamp_data_science/data/'
dataframe_movies=pd.read_csv(caminho_base + 'movies.csv')
dataframe_ratings=pd.read_csv(caminho_base + 'ratings.csv')

dataframe_merged = pd.merge(dataframe_movies, dataframe_ratings, on='movieId')

dataframe_merged.info()

df_grp = dataframe_merged.groupby('title')
df_grp_rating_mean = dataframe_merged.groupby('title')['rating'].mean().sort_values(ascending=False)
df_grp_rating_count = dataframe_merged.groupby('title')['rating'].count().sort_values(ascending=False)

to_delete  = [n for n, v in df_grp_rating_count.iteritems() if v < 10]

dataframe_merged.drop(dataframe_merged[dataframe_merged['title'].isin(to_delete)].index, inplace=True)

user_view = dataframe_merged.pivot_table(index='userId', columns='title', values='rating')

corr_matrix = user_view.corr(method='pearson', min_periods=50)

test_user = user_view.iloc[1].dropna()

similar_movies = pd.Series()
for i in range(len(test_user.index)):
    asss = test_user.index[i]
    s = corr_matrix[asss].dropna()
    s = s.map(lambda x: x*test_user[i])
    similar_movies=similar_movies.append(s)
    
to_delete_viewed = test_user.index.values
x = [c for c in to_delete_viewed]
similar_movies_matrix = [(c, v) for c, v in similar_movies.iteritems() if c not in to_delete_viewed]

similar_movies_matrix_sorted = sorted(similar_movies_matrix, key=lambda tup: tup[1], reverse=True)

for i in range(5):
    print(similar_movies_matrix_sorted[i])