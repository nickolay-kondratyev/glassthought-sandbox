import React, { memo } from 'react';
import { Handle, Position } from '@xyflow/react';

export default memo(({data, isConnectable}) => {
    return (
        <>
            <Handle
                type="target"
                position={Position.Left}
                style={{background: '#555'}}
                onConnect={(params) => console.log('handle onConnect', params)}
                isConnectable={isConnectable}
            />

            {/* Example Image */}
            <div style={{marginTop: '10px'}}>
                <img
                    src={data.imageUrl || 'https://via.placeholder.com/150'}
                    alt="Example"
                    style={{width: '100%', borderRadius: '8px'}}
                />
            </div>

            {/* Clickable Link */}
            <div style={{display: 'flex', justifyContent: 'center', alignItems: 'center', marginTop: '10px'}}>
                <a
                    href={data.linkUrl || '#'}
                    target="_blank"
                    rel="noopener noreferrer"
                    style={{color: '#1e90ff', textDecoration: 'none'}}
                >
                    {data.linkText || 'Click Here'}
                </a>
            </div>

            <Handle
                type="source"
                position={Position.Right}
                id="a"
                style={{top: 10, background: '#555'}}
                isConnectable={isConnectable}
            />
            <Handle
                type="source"
                position={Position.Right}
                id="b"
                style={{bottom: 10, top: 'auto', background: '#555'}}
                isConnectable={isConnectable}
            />
        </>
    );
});
