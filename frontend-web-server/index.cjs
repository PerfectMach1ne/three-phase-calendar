const { createServer } = require('node:http');

const HOSTNAME = '127.0.0.1';
const PORT = 3057;

const server = createServer((req, res) => {
	res.statusCode = 418;
	res.setHeader('Content-Type', 'text/plain');
	res.end('helooooo teapot wooorld!!!');
});

server.listen(PORT, HOSTNAME, () => {
	console.log(`Server running at http://${HOSTNAME}:${PORT}/`);
})
