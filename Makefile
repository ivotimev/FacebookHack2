all: install
	cd server && make

install:
	cd script && python initialize_config.py

purge: clean
	- rm -rf config

clean:
	cd server && make clean
