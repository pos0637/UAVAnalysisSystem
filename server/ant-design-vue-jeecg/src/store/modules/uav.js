const uav = {
    state: {
        dataViewPaths: [
            {
                id: 0,
                name: '轨迹1',
                description: '轨迹说明 轨迹说明 轨迹说明',
                visible: true,
                color: '#FF000080'
            },
            {
                id: 1,
                name: '轨迹2',
                description: '轨迹说明 轨迹说明 轨迹说明',
                visible: false,
                color: '#FF000080'
            },
            {
                id: 2,
                name: '轨迹3',
                description: '轨迹说明 轨迹说明 轨迹说明',
                visible: true,
                color: '#FF000080'
            },
            {
                id: 3,
                name: '轨迹4',
                description: '轨迹说明 轨迹说明 轨迹说明',
                visible: true,
                color: '#FF000080'
            },
            {
                id: 4,
                name: '轨迹5',
                description: '轨迹说明 轨迹说明 轨迹说明',
                visible: true,
                color: '#FF000080'
            }
        ]
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
        }
    }
};

export default uav;
