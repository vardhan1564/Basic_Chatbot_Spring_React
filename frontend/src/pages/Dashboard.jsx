import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

const Dashboard = () => {
    const [projects, setProjects] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [newProject, setNewProject] = useState({ name: '', systemPrompt: '' });
    const navigate = useNavigate();

    useEffect(() => { fetchProjects(); }, []);

    const fetchProjects = async () => {
        try {
            const res = await api.get('/projects');
            setProjects(res.data);
        } catch (err) { console.error(err); }
    };

    const handleCreate = async (e) => {
        e.preventDefault();
        try {
            await api.post('/projects', newProject);
            setShowModal(false);
            setNewProject({ name: '', systemPrompt: '' });
            fetchProjects();
        } catch (err) { alert("Failed to create agent"); }
    };

    return (
        <div className="container">
            <div className="dashboard-header">
                <h1>My AI Agents</h1>
                <div className="header-buttons">
                    <button className="btn-primary" onClick={() => setShowModal(true)}>+ New Agent</button>
                    <button className="btn-danger" onClick={() => { localStorage.clear(); navigate('/'); }}>Logout</button>
                </div>
            </div>

            {/* Agent Cards */}
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '20px' }}>
                {projects.map(p => (
                    <div key={p.id} onClick={() => navigate(`/chat/${p.id}`)} 
                         style={{ background: 'white', padding: '20px', borderRadius: '12px', cursor: 'pointer', boxShadow: '0 2px 4px rgba(0,0,0,0.05)' }}>
                        <h3>{p.name}</h3>
                        <p style={{ color: '#666', fontSize: '0.9rem', marginTop: '10px' }}>{p.systemPrompt?.substring(0, 80)}...</p>
                        <p style={{ marginTop: '15px', color: 'var(--primary)', fontWeight: '600' }}>Chat Now →</p>
                    </div>
                ))}
            </div>

            {/* CREATE AGENT MODAL */}
            {showModal && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <h2>Create New Agent</h2>
                        <form onSubmit={handleCreate}>
                            <input 
                                placeholder="Agent Name (e.g., Java Expert)" 
                                value={newProject.name}
                                onChange={e => setNewProject({...newProject, name: e.target.value})}
                                required 
                            />
                            <textarea 
                                placeholder="System Instructions (e.g., You are a strict Java tutor...)" 
                                value={newProject.systemPrompt}
                                onChange={e => setNewProject({...newProject, systemPrompt: e.target.value})}
                                required
                            />
                            <div style={{ display: 'flex', gap: '10px' }}>
                                <button type="submit" className="btn-primary" style={{ flex: 1 }}>Create</button>
                                <button type="button" className="btn-danger" style={{ flex: 1, backgroundColor: '#6b7280' }} onClick={() => setShowModal(false)}>Cancel</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Dashboard;