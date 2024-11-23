const position = {x: 0, y: 0};

export const initialNodes = [
    {
        id: '1',
        type: 'input',
        data: {label: 'input'},
        position,
    },
    {
        id: '2',
        type: 'imgNode',
        data: {
            label: 'node 2',
            imageUrl: 'https://www.glassthought.com/assets/images/DALL_E_2023_12_17_23.46.37___An_image_that_encapsulates_the_idea_of__Do__Embrace_Reality_and_Deal_with_It_._The_central_figure__a_Black_man_in_business_attire__stands_confidently_.png',
            linkText: "Clear Goals",
            linkUrl: "https://www.glassthought.com/notes/tb15wc67pe4zfqopw3m8c8f/"
        },
        height: 200,
        style: {border: '1px solid #777', padding: 10},
        position,
    },
    {
        id: '2a',
        data: {label: 'node 2a'},
        position,
    },
    {
        id: '2b',
        data: {label: 'node 2b'},
        position,
    },
    {
        id: '2c',
        data: {label: 'node 2c'},
        position,
    },
    {
        id: '2d',
        data: {label: 'node 2d'},
        position,
    },
    {
        id: '3',
        data: {label: 'node 3'},
        position,
    },
    {
        id: '4',
        data: {label: 'node 4'},
        position,
    },
    {
        id: '5',
        data: {label: 'node 5'},
        position,
    },
    {
        id: '6',
        type: 'output',
        data: {label: 'output'},
        position,
    },
    {id: '7', type: 'output', data: {label: 'output'}, position},
];

export const initialEdges = [
    {id: 'e12', source: '1', target: '2', type: 'smoothstep'},
    {id: 'e13', source: '1', target: '3', type: 'smoothstep'},
    {id: 'e22a', source: '2', target: '2a', type: 'smoothstep'},
    {id: 'e22d', source: '2', target: '2d', type: 'smoothstep'},
    {id: 'e22b', source: '2', target: '2b', type: 'smoothstep'},
    {id: 'e22c', source: '2', target: '2c', type: 'smoothstep'},
    {id: 'e2c2d', source: '2c', target: '2d', type: 'smoothstep'},
    {id: 'e45', source: '4', target: '5', type: 'smoothstep'},
    {id: 'e56', source: '5', target: '6', type: 'smoothstep'},
    {id: 'e57', source: '5', target: '7', type: 'smoothstep'},
];
