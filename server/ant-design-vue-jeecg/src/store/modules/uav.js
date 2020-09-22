const uav = {
    state: {
        dataViewPaths: [
            {
                id: 0,
                name: '轨迹1',
                description: '轨迹说明 轨迹说明 轨迹说明',
                visible: true,
                color: '#FF000080',
                points: [
                    {
                        lng: 121.814224,
                        lat: 31.156484,
                        alt: 50.1
                    },
                    {
                        lng: 121.808547,
                        lat: 31.165754,
                        alt: 50.1
                    },
                    {
                        lng: 121.796186,
                        lat: 31.184848,
                        alt: 50.1
                    },
                    {
                        lng: 121.788353,
                        lat: 31.192015,
                        alt: 50.1
                    },
                    {
                        lng: 121.771752,
                        lat: 31.194734,
                        alt: 50.1
                    },
                    {
                        lng: 121.74969,
                        lat: 31.189791,
                        alt: 50.1
                    },
                    {
                        lng: 121.754217,
                        lat: 31.179658,
                        alt: 50.1
                    },
                    {
                        lng: 121.757523,
                        lat: 31.167794,
                        alt: 50.1
                    }
                ]
            },
            {
                id: 1,
                name: '轨迹2',
                description: '轨迹说明 轨迹说明 轨迹说明',
                visible: true,
                color: '#00FF0080',
                points: [
                    {
                        lng: 121.814324,
                        lat: 31.126484,
                        alt: 60.1
                    },
                    {
                        lng: 121.808447,
                        lat: 31.135754,
                        alt: 60.1
                    },
                    {
                        lng: 121.796286,
                        lat: 31.174848,
                        alt: 60.1
                    },
                    {
                        lng: 121.788453,
                        lat: 31.112015,
                        alt: 60.1
                    },
                    {
                        lng: 121.771152,
                        lat: 31.154734,
                        alt: 60.1
                    },
                    {
                        lng: 121.74569,
                        lat: 31.179791,
                        alt: 60.1
                    },
                    {
                        lng: 121.756217,
                        lat: 31.139658,
                        alt: 60.1
                    },
                    {
                        lng: 121.752523,
                        lat: 31.127794,
                        alt: 60.1
                    }
                ]
            },
            {
                id: 2,
                name: '轨迹3',
                description: '轨迹说明 轨迹说明 轨迹说明',
                visible: true,
                color: '#FF000080',
                points: [
                    {
                        lng: 121.814324,
                        lat: 31.126484,
                        alt: 60.1
                    },
                    {
                        lng: 121.808447,
                        lat: 31.135754,
                        alt: 60.1
                    },
                    {
                        lng: 121.796286,
                        lat: 31.174848,
                        alt: 60.1
                    },
                    {
                        lng: 121.788453,
                        lat: 31.112015,
                        alt: 60.1
                    },
                    {
                        lng: 121.771152,
                        lat: 31.154734,
                        alt: 60.1
                    },
                    {
                        lng: 121.74569,
                        lat: 31.179791,
                        alt: 60.1
                    },
                    {
                        lng: 121.756217,
                        lat: 31.139658,
                        alt: 60.1
                    },
                    {
                        lng: 121.752523,
                        lat: 31.127794,
                        alt: 60.1
                    }
                ]
            },
            {
                id: 3,
                name: '轨迹4',
                description: '轨迹说明 轨迹说明 轨迹说明',
                visible: true,
                color: '#FF000080',
                points: [
                    {
                        lng: 121.814324,
                        lat: 31.126484,
                        alt: 60.1
                    },
                    {
                        lng: 121.808447,
                        lat: 31.135754,
                        alt: 60.1
                    },
                    {
                        lng: 121.796286,
                        lat: 31.174848,
                        alt: 60.1
                    },
                    {
                        lng: 121.788453,
                        lat: 31.112015,
                        alt: 60.1
                    },
                    {
                        lng: 121.771152,
                        lat: 31.154734,
                        alt: 60.1
                    },
                    {
                        lng: 121.74569,
                        lat: 31.179791,
                        alt: 60.1
                    },
                    {
                        lng: 121.756217,
                        lat: 31.139658,
                        alt: 60.1
                    },
                    {
                        lng: 121.752523,
                        lat: 31.127794,
                        alt: 60.1
                    }
                ]
            },
            {
                id: 4,
                name: '轨迹5',
                description: '轨迹说明 轨迹说明 轨迹说明',
                visible: true,
                color: '#FF000080',
                points: [
                    {
                        lng: 121.814324,
                        lat: 31.126484,
                        alt: 60.1
                    },
                    {
                        lng: 121.808447,
                        lat: 31.135754,
                        alt: 60.1
                    },
                    {
                        lng: 121.796286,
                        lat: 31.174848,
                        alt: 60.1
                    },
                    {
                        lng: 121.788453,
                        lat: 31.112015,
                        alt: 60.1
                    },
                    {
                        lng: 121.771152,
                        lat: 31.154734,
                        alt: 60.1
                    },
                    {
                        lng: 121.74569,
                        lat: 31.179791,
                        alt: 60.1
                    },
                    {
                        lng: 121.756217,
                        lat: 31.139658,
                        alt: 60.1
                    },
                    {
                        lng: 121.752523,
                        lat: 31.127794,
                        alt: 60.1
                    }
                ]
            }
        ],
        showAltitude: true
    },
    mutations: {
        setPaths(state, paths) {
            state.dataViewPaths = paths;
        },
        addPath(state, path) {
            state.dataViewPaths.push(path);
        },
        removePath(state, id) {
            for (const path of state.dataViewPaths) {
                if (path.id === id) {
                    state.dataViewPaths = state.dataViewPaths.filter(item => item !== path);
                    return;
                }
            }
        },
        switchPathVisible(state, id) {
            for (const path of state.dataViewPaths) {
                if (path.id === id) {
                    path.visible = !path.visible;
                    return;
                }
            }
        },
        setPathColor(state, id, color) {
            for (const path of state.dataViewPaths) {
                if (path.id === id) {
                    path.color = color;
                    return;
                }
            }
        },
        switchShowAltitude(state) {
            state.showAltitude = !state.showAltitude;
        }
    }
};

export default uav;
