import plotly
import plotly.plotly as py
from IPython.core.display import Math
from plotly.graph_objs import *
import json
import igraph as ig

plotly.tools.set_credentials_file(username='JiayuChen4e67', api_key='CRalX31BWg9tW5KCaLfe')
data = []
with open('data.json') as json_data:
    data = json.load(json_data)

print(data.keys())
#[u'nodes', u'links']
N=len(data['nodes'])
print('node size: ', N)

# Define the list of edges
# and the Graph object from Edges:
L=len(data['links'])
print('link size', L)
Edges=[(data['links'][k]['source'], data['links'][k]['target']) for k in range(L)]

G=ig.Graph(Edges, directed=False)

labels=[]
group=[]
vals = []
for node in data['nodes']:
    labels.append(node['name'] + ' ' + str(node['val']))
    group.append(node['group'])
    vals.append(node['val'])

# normalize vals
max_val = max(vals)
min_val = min(vals)
for i in range(len(vals)):
    vals[i] = min(10, (vals[i]-min_val)/(max_val - min_val) * 10000000)
# print(vals)
# layt is a list of three elements lists (the coordinates of nodes):
layt=G.layout('kk', dim=3)
print("layt.size", len(layt))

Xn=[layt[k][0] for k in range(N)]# x-coordinates of nodes
Yn=[layt[k][1] for k in range(N)]# y-coordinates
Zn=[layt[k][2] for k in range(N)]# z-coordinates
Xe=[]
Ye=[]
Ze=[]
for e in Edges:
    Xe+=[layt[e[0]][0],layt[e[1]][0], None]# x-coordinates of edge ends
    Ye+=[layt[e[0]][1],layt[e[1]][1], None]
    Ze+=[layt[e[0]][2],layt[e[1]][2], None]


import plotly.plotly as py
from plotly.graph_objs import *
trace1=Scatter3d(x=Xe,
               y=Ye,
               z=Ze,
               mode='lines',
               line=Line(color='rgb(125,125,125)', width=1),
               hoverinfo='none'
               )
trace2=Scatter3d(x=Xn,
               y=Yn,
               z=Zn,
               mode='markers',
               name='actors',
               marker=Marker(symbol='dot',
                             size=vals,
                             color=group,
                             colorscale='Viridis',
                             line=Line(color='rgb(50,50,50)', width=0.5)
                             ),
               text=labels,
               hoverinfo='text'
               )

axis=dict(showbackground=False,
          showline=False,
          zeroline=False,
          showgrid=False,
          showticklabels=False,
          title=''
          )

layout = Layout(
         title="Network of movies & actors<br> Les Miserables (3D)",
         width=1000,
         height=1000,
         showlegend=False,
         scene=Scene(
         xaxis=XAxis(axis),
         yaxis=YAxis(axis),
         zaxis=ZAxis(axis),
        ),
     margin=Margin(
        t=100
    ),
    hovermode='closest',
    annotations=Annotations([
           Annotation(
           showarrow=False,
            text="Data source: <a href='http://bost.ocks.org/mike/miserables/miserables.json'>[1]</a>",
            xref='paper',
            yref='paper',
            x=0,
            y=0.1,
            xanchor='left',
            yanchor='bottom',
            font=Font(
            size=14
            )
            )
        ]),    )

data1 = Data([trace1, trace2])
fig=Figure(data=data1, layout=layout)

py.iplot(fig, filename='Les-Miserables')



